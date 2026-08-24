import { Component } from '@angular/core';
import { ProdutoListComponent } from './features/produtos/components/produto-list/produto-list';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ProdutoListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  title = 'frontend-produtos';
}
