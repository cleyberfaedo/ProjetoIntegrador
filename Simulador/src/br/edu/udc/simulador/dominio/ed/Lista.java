package br.edu.udc.simulador.dominio.ed;

public class Lista<T> {

	private class No {
		public No anterior;
		public No proximo;
		public T dado;

		public No(No proximo, No anterior, T dado) {
			this.anterior = anterior;
			this.proximo = proximo;
			this.dado = dado;
		}

		public No(T dado) {
			this.anterior = null;
			this.proximo = null;
			this.dado = dado;
		}
	}

	private class IteratorLista implements IteradorManipulador<T> {

		private No cursor;

		public IteratorLista(No no) {
			this.cursor = no;
		}

		@Override
		public void anterior() {
			if (cursor != null) {
				this.cursor = this.cursor.anterior;
			} else {
				throw new IndexOutOfBoundsException("N�o existe anterior para ser iterado");
				// TODO procurar uma exe��o melhor para este caso
			}
		}

		@Override
		public void proximo() {
			this.cursor = this.cursor.proximo;
		}

		@Override
		public boolean temProximo() {
			if (this.cursor != null) {
				return true;
			}
			return false;
		}

		@Override
		public boolean temAnterior() {
			if (this.cursor != null) {
				return true;
			}
			return false;
		}

		@Override
		public T getDado() {
			return this.cursor.dado;
		}

		@Override
		public void adicionaDepois(T elementoAdicionado) {

			No noAdicionado;

			if (elementoAdicionado == null)
				throw new NullPointerException();

			if (this.cursor == null) {// lista vazia
				noAdicionado = new No(elementoAdicionado);
				this.cursor = inicio = fim = noAdicionado;

			} else {// j� existem elementos na lista

				noAdicionado = new No(this.cursor.proximo, this.cursor, elementoAdicionado);

				if (this.cursor.proximo != null) {
					this.cursor.proximo.anterior = noAdicionado;
				} else {
					// this.cursor esta no fim da lista, ou seja, foi criado um
					// No que deveria esta no fim, mas sem a referencia de fim
					fim = this.cursor.proximo;// elemento recem criado
				}

				this.cursor.proximo = noAdicionado;
			}
			tamanho++;
		}

		@Override
		public void adicionaAntes(T elementoAdicionado) {

			No noAdicionado;

			if (elementoAdicionado == null)
				throw new NullPointerException();

			if (this.cursor == null) {// lista vazia
				noAdicionado = new No(elementoAdicionado);
				this.cursor = inicio = fim = noAdicionado;

			} else {// j� existem elementos na lista

				noAdicionado = new No(this.cursor, this.cursor.anterior, elementoAdicionado);

				if (this.cursor.anterior != null) {
					this.cursor.anterior.proximo = noAdicionado;
				} else {
					// this.cursor esta no inicio da lista
					inicio = this.cursor.anterior;// elemento recem criado
				}

				this.cursor.anterior = noAdicionado;
			}
			tamanho++;
		}

		@Override
		public void remove() {

			if (this.cursor == null) {// lista vazia
				throw new RuntimeException("Fila est� vazia! N�o h� elementos para remover");

			} else if (inicio == fim) {// existe apenas um elemento na lista
				inicio = fim = this.cursor = null;

			} else {// existe mais de um elemento na lista
				No noRemovido = this.cursor;

				// Posiciona o cursor em outro elemento para que
				// noRemovido seja deletado
				if (noRemovido.proximo != null) {
					this.cursor = this.cursor.proximo;
				} else {
					this.cursor = this.cursor.anterior;
				}
				// Se ele for o unico elemento da lista, o cursor ir� reeber
				// anterior que � null, previsto pela ideia do Iterador

				if (noRemovido.proximo != null) {
					noRemovido.proximo.anterior = noRemovido.anterior;
				} else {// elemento esta no fim da lista
					fim = fim.anterior;
					fim.proximo = null;
				}

				if (noRemovido.anterior != null) {
					noRemovido.anterior.proximo = noRemovido.proximo;
				} else {// esta no inicio da lista
					inicio = inicio.proximo;
					inicio.anterior = null;
				}
			}
			tamanho--;
		}

	}

	public IteradorManipulador<T> inicio() {
		return new IteratorLista(this.inicio);
	}

	public IteradorManipulador<T> fim() {
		return new IteratorLista(this.fim);
	}

	private No inicio;
	private No fim;

	private int tamanho;

	public Lista() {
		this.inicio = this.fim = null;
		this.tamanho = 0;
	}

	public int tamanho() {
		return this.tamanho;
	}

	public void adiciona(T elementoAdicionado, int posicao) {

		No noAdicionado;

		if (elementoAdicionado == null)
			throw new NullPointerException();

		if (inicio == null) {// lista vazia

			noAdicionado = new No(elementoAdicionado);
			inicio = fim = noAdicionado;
		} else {// j� existem elementos na lista

			if (posicao <= 1) {// inserir no inicio da lista

				noAdicionado = new No(inicio, null, elementoAdicionado);
				inicio.anterior = noAdicionado;
				inicio = noAdicionado;
			} else if (posicao >= tamanho) {// inserir no final da lista

				noAdicionado = new No(null, fim, elementoAdicionado);
				fim.proximo = noAdicionado;
				fim = noAdicionado;
			} else {// inserir no meio da lista

				No cursor = inicio;
				while (posicao > 0) {
					cursor = cursor.proximo;
					posicao--;
				}
				// inserir na posi��o de cursor
				noAdicionado = new No(cursor, cursor.anterior, elementoAdicionado);
				cursor.anterior.proximo = noAdicionado;
				cursor.anterior = noAdicionado;
			}
		}
		this.tamanho++;
	}

	public void adiciona(T elementoAdicionado) {

		// adiciona no fim da lista
		this.adiciona(elementoAdicionado, this.tamanho + 1);
	}

	public int adiciona(T[] grupoElementos) {

		int numeroElementosAdicionados = 0;

		for (int i = 0; i < grupoElementos.length; i++) {

			try {
				this.adiciona(grupoElementos[i]);

			} catch (NullPointerException e) {
				numeroElementosAdicionados--;
			}

			numeroElementosAdicionados++;
		}

		return numeroElementosAdicionados;
	}

	public void remove(int posicao) {

		if (inicio == null) {// se a lista est� vazia
			throw new RuntimeException("Fila est� vazia! N�o h� elementos para remover");
		}

		if (inicio == fim) {// se existe apenas um elemento na lista
			inicio = fim = null;
			tamanho--;
		}

		if (posicao <= 1) {// remover o primeiro elemento da lista
			inicio = inicio.proximo;
			inicio.anterior = null;

		} else if (posicao >= tamanho) {// remover o �ltimo elemento da lista
			fim = fim.anterior;
			fim.proximo = null;

		} else {// remover um elemento no meio da lista

			No cursor = inicio;
			while (posicao > 0) {
				cursor = cursor.proximo;
				posicao--;
			}
			// remover o elemento cursor
			cursor.anterior.proximo = cursor.proximo;
			cursor.proximo.anterior = cursor.anterior;
		}
		this.tamanho--;
	}

	public T consulta(int pos) {
		if (inicio == null)// se a lista est� vazia
			return null;

		if (inicio == fim)// se existe apenas um elemento na lista
			return inicio.dado;

		if (pos == 0)// consulta o primeiro elemento da lista
			return inicio.dado;

		else if (pos >= tamanho - 1)// consulta o �ltimo elemento da lista
			return fim.dado;

		else // consulta um elemento no meio da lista
		{
			No cursor = inicio;
			while (pos > 0) {
				cursor = cursor.proximo;
				pos--;
			}
			// consulta o elemento aux
			return cursor.dado;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {

		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;

		No cursor = this.inicio;
		Iterator<T> it = ((Lista<T>) obj).inicio();

		while (it.temProximo()) {
			if (!(cursor.dado.equals(it.getDado()))) {
				return false;
			}

			cursor = cursor.proximo;
			it.proximo();
		}

		return true;
	}
}