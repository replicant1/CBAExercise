package com.bailey.rod.cbaexercise.dagger

import android.app.Application
import com.bailey.rod.cbaexercise.repo.AccountRepository
import com.bailey.rod.cbaexercise.viewmodel.AccountOverviewViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Dagger application component. Any class that wants to have objects dependency injected
 * into it needs to register with this class  and put the following code:
 * <pre>
 *     init { CbaApplication.graph.inject(this) }
 * </pre>
 */
@Singleton
@Component(modules = [AppModule::class, DbModule::class])
interface AppComponent {
    fun inject(app: Application)
    fun inject(viewModel: AccountOverviewViewModel)
    fun inject(repo: AccountRepository)
}