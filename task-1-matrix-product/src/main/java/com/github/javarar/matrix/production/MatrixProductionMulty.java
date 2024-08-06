package com.github.javarar.matrix.production;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MatrixProductionMulty extends RecursiveTask<Integer> {
	private final Matrix a;
	private final Matrix b;
	private final int i;
	private final int j;
	
	public MatrixProductionMulty(Matrix a, Matrix b, int i, int j) {
		this.a = a;
		this.b = b;
		this.i = i;
		this.j = j;
	}
	
	public static void main(String[] args) {
		int[][] matrix = {{1, 2}, {3, 4}, {0, 0}};
		Matrix matrix1 = new Matrix(matrix);
		Matrix matrix2 = new Matrix(new int[][]{{5, 6}, {7, 8}});
		System.out.println(matrix1);
		System.out.println();
		System.out.println(matrix2);
		System.out.println(product(matrix1, matrix2));
		System.out.println(productMultiThreading(matrix1, matrix2, 10));
		
	}
	
	public static Matrix product(Matrix a, Matrix b) {
		int[][] ints = new int[a.matrix.length][b.matrix.length];
		for (int i = 0; i < a.matrix.length; i++) {
			for (int j = 0; j < a.matrix[i].length; j++) {
				int sum = 0;
				for (int k = 0; k < b.matrix.length; k++) {
					int first = a.matrix[i][k];
					int second = b.matrix[k][j];
					sum += first * second;
				}
				ints[i][j] = sum;
			}
		}
		return new Matrix(ints);
	}
	
	public static Matrix productMultiThreading(Matrix a, Matrix b, int threads) {
		ForkJoinPool forkJoinPool = new ForkJoinPool(threads);
			int[][] ints = new int[a.matrix.length][b.matrix.length];
			Map<Integer[], ForkJoinTask<Integer>> forkJoinTaskMap = new ConcurrentHashMap<>();
			for (int i = 0; i < a.matrix.length; i++) {
				for (int j = 0; j < a.matrix[i].length; j++) {
					MatrixProductionMulty matrixProduction = new MatrixProductionMulty(a, b, i, j);
					forkJoinTaskMap.put(new Integer[]{i, j}, forkJoinPool.submit(matrixProduction));
				}
			}
			forkJoinTaskMap.forEach((key, value) -> ints[key[0]][key[1]] = value.join());
			return new Matrix(ints);
		}
	
	@Override
	protected Integer compute() {
		int sum = 0;
		for (int k = 0; k < b.matrix.length; k++) {
			int first = a.matrix[i][k];
			int second = b.matrix[k][j];
			sum += first * second;
		}
		return sum;
	}
	
	@Getter
	public static class Matrix {
		private final int[][] matrix;
		
		public Matrix(int[][] matrix) {
			this.matrix = matrix;
		}
		
		@Override
		public String toString() {
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < matrix.length; i++) {
				stringBuffer.append("|");
				stringBuffer.append("\t");
				for (int j = 0; j < matrix[i].length; j++) {
					stringBuffer.append(matrix[i][j]);
					stringBuffer.append("\t");
				}
				stringBuffer.append("|\n");
			}
			return stringBuffer.toString();
		}
	}
}
