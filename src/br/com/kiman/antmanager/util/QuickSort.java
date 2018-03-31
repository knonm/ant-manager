package br.com.kiman.antmanager.util;

import java.util.Comparator;

public class QuickSort<T extends Comparable<T>> {

	private Comparator<T> comp;

	private int particao(T[] col, int min, int max) {
		T buff;
		int ind;
		int indPivo = (min + max) / 2;
		T valorPivo = col[indPivo];

		buff = col[max];
		col[max] = col[indPivo];
		col[indPivo] = buff;

		ind = min;
		for (int i = min; i < max; i++) {
			if (this.comp.compare(col[i], valorPivo) < 0) {
				buff = col[i];
				col[i] = col[ind];
				col[ind] = buff;
				ind++;
			}
		}

		buff = col[max];
		col[max] = col[ind];
		col[ind] = buff;

		return ind;
	}

	private void ordena(T[] col, int min, int max) {
		int p;
		if (min < max) {
			p = particao(col, min, max);
			ordena(col, min, p - 1);
			ordena(col, p + 1, max);
		}
	}

	public void ordena(T[] col) {
		ordena(col, 0, col.length - 1);
	}

	public QuickSort(Comparator<T> comp) {
		this.comp = comp;
	}

	public QuickSort() {
		this.comp = new QuickSortComparator();
	}

	private class QuickSortComparator implements Comparator<T> {

		@Override
		public int compare(T o1, T o2) {
			return o1.compareTo(o2);
		}

	}
}
