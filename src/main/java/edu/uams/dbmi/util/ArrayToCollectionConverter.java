package edu.uams.dbmi.util;

import java.util.ArrayList;
import java.util.List;

public class ArrayToCollectionConverter {

	
	public static <T> List<T> convertArrayToCollection(T[] array) {
		ArrayList<T> list = new ArrayList<T>();
		for (T t : array) {
			list.add(t);
		}
		return list;
	}
	
	public static List<Integer> convertArrayToCollection(int[] array) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i : array) {
			list.add(i);
		}
		return list;
	}
	
	public static List<Float> convertArrayToCollection(float[] array) {
		ArrayList<Float> list = new ArrayList<Float>();
		for (float i : array) {
			list.add(i);
		}
		return list;
	}
	
	public static List<Long> convertArrayToCollection(long[] array) {
		ArrayList<Long> list = new ArrayList<Long>();
		for (long i : array) {
			list.add(i);
		}
		return list;
	}
	
	public static List<Double> convertArrayToCollection(double[] array) {
		ArrayList<Double> list = new ArrayList<Double>();
		for (double i : array) {
			list.add(i);
		}
		return list;
	}
	
	public static List<Short> convertArrayToCollection(short[] array) {
		ArrayList<Short> list = new ArrayList<Short>();
		for (short i : array) {
			list.add(i);
		}
		return list;
	}
	
	public static List<Byte> convertArrayToCollection(byte[] array) {
		ArrayList<Byte> list = new ArrayList<Byte>();
		for (byte i : array) {
			list.add(i);
		}
		return list;
	}
}
