public class SatBitOps {
	private static int i() {
		int value = 3;
		value &= 1;
		value |= 4;
		value ^= 4;
		value <<= 2;
		value >>= 1;
		value = -value;
		value >>>= 1;
		return value;
	}

//	private static long l() {
//		long value = 3L;
//		value &= 1L;
//		value |= 4L;
//		value ^= 4L;
//		value <<= 2L;
//		value >>= 1L;
//		value = -value;
//		value >>>= 1L;
//		return value;
//	}


	public static void main(String[] args) {
		assert i() == 2147483647;
//		assert l() == 9223372036854775807L;
	}
}
