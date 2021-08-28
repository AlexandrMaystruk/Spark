package com.gmail.maystruks08.domain

import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchersImpl
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import com.gmail.maystruks08.domain.use_cases.ProvideMessageUseCase
import com.gmail.maystruks08.domain.use_cases.ProvideMessageUseCaseImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import java.util.*

@ExperimentalCoroutinesApi
class ProvideMessageUseCaseTest {

    @Mock
    lateinit var repository: Repository

    private lateinit var provideMessageUseCase: ProvideMessageUseCase
    private lateinit var coroutinesDispatchers: CoroutineDispatchers

    @Before
    fun before() {
        repository = mock(Repository::class.java)
        coroutinesDispatchers = CoroutineDispatchersImpl()
        provideMessageUseCase = ProvideMessageUseCaseImpl(coroutinesDispatchers, repository)
    }

    @Test
    fun checkIsProvideMessageUseCaseReturnMessageById() = runBlockingTest {
        assert(::repository.isInitialized)
        val expectedMessage = Message(
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
        `when`(repository.provideMessageById(expectedMessage.id)).thenReturn(expectedMessage)
        val receivedMessage = provideMessageUseCase.invoke(expectedMessage.id)
        assertEquals(expectedMessage, receivedMessage)
        verify(repository).provideMessageById(expectedMessage.id)
        verifyNoMoreInteractions(repository)
    }
}