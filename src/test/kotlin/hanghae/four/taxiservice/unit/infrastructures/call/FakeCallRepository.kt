package hanghae.four.taxiservice.unit.infrastructures.call

import hanghae.four.taxiservice.domain.taxi.call.Call
import hanghae.four.taxiservice.domain.taxi.call.CallReader
import hanghae.four.taxiservice.domain.taxi.call.CallStore
import hanghae.four.taxiservice.infrastructure.taxi.call.exception.CallNotFoundException
import java.util.concurrent.atomic.AtomicLong

internal class FakeCallRepository : CallStore, CallReader {
    private val autoGeneratedId: AtomicLong = AtomicLong(0)
    private val callStore = mutableListOf<Call>()

    override fun store(call: Call): Call {
        if (call.id == null) {
            val saveCall = Call(
                id = autoGeneratedId.incrementAndGet(),
                userId = call.userId,
                callNumber = call.callNumber,
                createdAt = call.createdAt,
                origin = call.origin,
                destination = call.destination,
                status = call.status,
                taxiId = call.taxiId
            )

            callStore.add(saveCall)
            return saveCall
        }

        callStore.removeIf { t -> t.id == call.id }
        callStore.add(call)
        return call
    }

    override fun getById(callId: Long): Call {
        return callStore.find { it.id == callId } ?: throw CallNotFoundException()
    }

    override fun findCall(callId: Long): Call {
        return callStore.find { it.id == callId } ?: throw CallNotFoundException()
    }
}
