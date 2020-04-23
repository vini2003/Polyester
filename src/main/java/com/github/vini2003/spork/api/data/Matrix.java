package com.github.vini2003.spork.api.data;

/**
 * A Matrix is a basic implementation
 * of a mathematical matrix.
 */
public class Matrix {
	public double[][] data;

	public int rows;
	public int columns;

	private static Matrix getMatrixForRotationX(double theta) {
		return new Matrix(new double[][]{
				{ 1, 0, 0 },
				{ 0, Math.cos(theta), -Math.sin(theta) },
				{ 0, Math.sin(theta),  Math.cos(theta) }
		});
	}

	private static Matrix getMatrixForRotationY(double theta) {
		return new Matrix(new double[][]{
				{  Math.cos(theta), 0, Math.sin(theta) },
				{ 0, 1, 0 },
				{ -Math.sin(theta), 0, Math.cos(theta) }
		});
	}

	private static Matrix getMatrixForRotationZ(double theta) {
		return new Matrix(new double[][]{
				{ Math.cos(theta), -Math.sin(theta), 0},
				{ Math.sin(theta), Math.cos(theta), 0},
				{ 0, 0, 1 }
		});
	}

	public Matrix(double[][] data) {
		this.data = data;
		this.rows = data.length;
		this.columns = data[0].length;
	}

	public Matrix multiply(Matrix target) {
		if (this.rows != target.columns && target.rows != this.columns) return null;

		int cMax = target.columns;
		int rMax = this.rows;

		double[][] newData = new double[cMax][rMax];
		for (int c = 0; c < cMax; ++c) {
			double[] column = new double[rMax];
			for (int r = 0; r < rMax; ++r) {
				column[r] += (target.data[r][c] * this.data[r][r]);
			}
			newData[c] = column;
		}

		return new Matrix(newData);
	}

	public Matrix rotateX(double theta) {
		return multiply(getMatrixForRotationX(theta));
	}

	public Matrix rotateY(double theta) {
		return multiply(getMatrixForRotationY(theta));
	}

	public Matrix rotateZ(double theta) {
		return multiply(getMatrixForRotationZ(theta));
	}

	public Matrix rotate(double alpha, double beta, double gamma) {
		Matrix result  = this;
		if (alpha != 0) result = result.rotateX(alpha);
		if (beta != 0) result = result.rotateY(beta);
		if (gamma != 0) result = result.rotateZ(gamma);
		return result;
	}
}
