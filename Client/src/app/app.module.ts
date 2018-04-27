import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes, RouterOutlet  } from '@angular/router';
import { AppComponent } from './app.component';
import { IdentificationComponent } from './identification/identification.component';
import { ExplorerComponent } from './explorer/explorer.component';
import { CreateaccountComponent } from './createaccount/createaccount.component';

const appRoutes: Routes = [
  { path : '', component : IdentificationComponent},
  { path: 'explorer', component: ExplorerComponent},
  { path : 'createaccount', component : CreateaccountComponent},
  { path : '**', redirectTo: '', pathMatch: 'full'},
];

@NgModule({
  declarations: [
    AppComponent,
    IdentificationComponent,
    ExplorerComponent,
    CreateaccountComponent,
  ],
  imports: [
    BrowserModule,
	RouterModule.forRoot(
      appRoutes,
      { enableTracing: false } // <-- debugging purposes only
    )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
