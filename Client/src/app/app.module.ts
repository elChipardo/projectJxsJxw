import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes, RouterOutlet  } from '@angular/router';
import { AppComponent } from './app.component';
import { IdentificationComponent } from './identification/identification.component';
import { ExplorerComponent } from './explorer/explorer.component';

const appRoutes: Routes = [
  { path : '', component : IdentificationComponent},
  { path: 'explorer', component: ExplorerComponent},
  { path : '**', redirectTo: '', pathMatch: 'full'},
];

@NgModule({
  declarations: [
    AppComponent,
    IdentificationComponent,
    ExplorerComponent,
  ],
  imports: [
    BrowserModule,
	RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
