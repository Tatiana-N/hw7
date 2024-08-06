package com.github.javarar.matrix.production;

import org.junit.jupiter.api.Test;

import static com.github.javarar.matrix.production.MatrixProductionMulty.product;
import static com.github.javarar.matrix.production.MatrixProductionMulty.productMultiThreading;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MatrixProductionTest {
	
	@Test
	public void validateMatrixProduction() {
		MatrixProductionMulty.Matrix matrix1 = new MatrixProductionMulty.Matrix(matrixProducer(1000));
		MatrixProductionMulty.Matrix matrix2 = new MatrixProductionMulty.Matrix(matrixProducer(1000));
		long time1 = System.currentTimeMillis();
		MatrixProductionMulty.Matrix stupid = product(matrix1, matrix2);
		long time2 = System.currentTimeMillis();
		MatrixProductionMulty.Matrix firstMultiThreadSolvation = productMultiThreading(matrix1, matrix2, 40);
		long time3 = System.currentTimeMillis();
		MatrixProductionMulty.Matrix secondMultiThreadSolvation = MatrixProductionMulty2.parallelProduct(matrix1, matrix2,
				50);
		long time4 = System.currentTimeMillis();
		System.out.printf("firstMultiThreadSolvation %s secondMultiThreadSolvation %s stupid %s", time4 - time3,
				time3 - time2, time2 - time1);
		assertArrayEquals(stupid.getMatrix(), secondMultiThreadSolvation.getMatrix());
		assertArrayEquals(stupid.getMatrix(), firstMultiThreadSolvation.getMatrix());
		assertArrayEquals(firstMultiThreadSolvation.getMatrix(), secondMultiThreadSolvation.getMatrix());
	}
	
	private static int[][] matrixProducer(int length) {
		int[][] ints = new int[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				ints[i][j] = (int) (Math.random() * 1000);
			}
		}
		return ints;
	}
}
