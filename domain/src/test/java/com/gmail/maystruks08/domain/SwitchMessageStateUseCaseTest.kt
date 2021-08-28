package com.gmail.maystruks08.domain

import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import com.gmail.maystruks08.domain.use_cases.ProvideMessageUseCase
import com.gmail.maystruks08.domain.use_cases.SwitchReadReadMessageStateUseCase
import com.gmail.maystruks08.domain.use_cases.SwitchReadReadMessageStateUseCaseImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

@ExperimentalCoroutinesApi
class SwitchMessageStateUseCaseTest {

    @Mock
    lateinit var repository: Repository

    @Mock
    lateinit var provideMessageUseCase: ProvideMessageUseCase

    private lateinit var switchReadReadMessageStateUseCase: SwitchReadReadMessageStateUseCase

    @Before
    fun before() {
        repository = Mockito.mock(Repository::class.java)
        provideMessageUseCase = Mockito.mock(ProvideMessageUseCase::class.java)
        switchReadReadMessageStateUseCase =
            SwitchReadReadMessageStateUseCaseImpl(repository, provideMessageUseCase)
    }

    @Test
    fun checkIsCorrectChangeMessageState() = runBlockingTest {
        assert(::repository.isInitialized)
        val message = Message(
            id = "1",
            date = Date(),
            from = "Random message",
            header = "New message simulation",
            subject = "test",
            contentPreview = "Test content",
            group = "Push mock",
            content = "",
            isRead = false,
            isDeleted = false
        )
        Mockito.`when`(provideMessageUseCase.invoke(message.id)).thenReturn(message)
        Mockito.`when`(repository.updateMessage(message)).thenReturn(Unit)

        val receivedMessage = switchReadReadMessageStateUseCase.invoke(message.id)

        val expectedMessage = message.copy(isRead = true)

        assertEquals(expectedMessage, receivedMessage)

        Mockito.verify(provideMessageUseCase).invoke(message.id)
        Mockito.verify(repository).updateMessage(message)
        Mockito.verifyNoMoreInteractions(repository)
    }
}