class A extends Exception {
}

class UnsatThrows {
	public static Exception myA = new A();

	public static void main(String[] args) {
		if (myA instanceof A) {
			assert false;
		} 
	}
}
