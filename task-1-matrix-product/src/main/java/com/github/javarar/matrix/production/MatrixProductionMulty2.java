package com.github.javarar.matrix.production;


import java.util.Map;
import java.util.concurrent.*;

public class MatrixProductionMulty2 extends RecursiveTask<MatrixProductionMulty.Matrix> {
	private int number_of_threads;
	private MatrixProductionMulty.Matrix a;
	private MatrixProductionMulty.Matrix b;
	private MatrixProductionMulty.Matrix rez;
	private Integer index;
	
	@Override
	protected MatrixProductionMulty.Matrix compute() {
		int sum = 0;
		for (int j = 0; j < b.getMatrix()[0].length; j++) {
			for (int k = 0; k < a.getMatrix()[0].length; k++) {
				rez.getMatrix()[0][j] += a.getMatrix()[this.index][k] * b.getMatrix()[k][j];
			}
		}
		return rez;
	}
	
	MatrixProductionMulty2(MatrixProductionMulty.Matrix a, MatrixProductionMulty.Matrix b, Integer index) {
		this.a = a;
		this.b = b;
		this.index = index;
		this.rez = new MatrixProductionMulty.Matrix(new int[1][b.getMatrix()[0].length]);
	}
	
	public static MatrixProductionMulty.Matrix parallelProduct(MatrixProductionMulty.Matrix a,
	                                                           MatrixProductionMulty.Matrix b, int threads) {
		int[][] ints = new int[a.getMatrix().length][b.getMatrix().length];
		ForkJoinPool forkJoinPool = new ForkJoinPool(threads);
		Map<Integer[], ForkJoinTask<MatrixProductionMulty.Matrix>> forkJoinTaskMap = new ConcurrentHashMap<>();
		for (int i = 0; i < a.getMatrix().length; i++) {
			MatrixProductionMulty2 parallelMatrixProduct = new MatrixProductionMulty2(a, b, i);
			forkJoinTaskMap.put(new Integer[]{i}, forkJoinPool.submit(parallelMatrixProduct));
		}
		forkJoinTaskMap.forEach((key, value) -> {
			int[][] matrix = value.join().getMatrix();
			ints[key[0]] = matrix[0];
		});
		return new MatrixProductionMulty.Matrix(ints);
	}
}