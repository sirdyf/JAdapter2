// Инструмент контроля для отладки.
package autopartner;

public class Asrt {

	public static void perr(String msg) {
		System.err.println(msg);
	}

	public  static void is_true(boolean exp) {
		if (!exp) {
			perr("Assertion failed");
		}
	}

	public  static void is_int(Object exp) {
		if (exp == null) {
			perr("Assertion failed Object=null");
			return;
		}
		if (exp.getClass() != java.lang.Integer.class) {
			perr("Assertion failed not INT");
		}
	}

	public  static void is_bool(Object exp) {
		if (exp == null) {
			perr("Assertion failed Object=null");
			return;
		}
		if (exp.getClass() != java.lang.Boolean.class) {
			perr("Assertion failed not Boolean");
		}
	}

	public  static void is_data(Object exp) {
		if (exp == null) {
			perr("Assertion failed Object=null");
			return;
		}
		if (exp.getClass() != java.sql.Date.class) {
			perr("Assertion failed not Date");
		}
	}

	public  static void is_null(Object exp) {
		if (exp == null) {
			perr("Assertion failed Object=null");
		}
	}

	public  static boolean is_str(Object exp) {
		if (exp == null) {
			perr("Assertion failed Object=null");
			return false;
		}
		if (exp.getClass()!=java.lang.String.class){
			perr("Assertion: is not string!");
			return false;
		}
		if (exp.toString().length() < 1) {
			perr("Assertion failed String=''");
			return false;
		}
		return true;
	}

	public  static void is_false(boolean exp) {
		if (exp) {
			perr("Assertion failed");
		}
	}

	public  static void is_true(boolean exp, String msg) {
		if (!exp) {
			perr("Assertion failed: " + msg);
		}
	}

	public  static void is_false(boolean exp, String msg) {
		if (exp) {
			perr("Assertion failed: " + msg);
		}
	}

	//public static boolean is_int_positive_or_null(int i){}
	public static boolean is_int_negative(int i){
		if (i<0) {
			perr("The number:"+i+" is negative!");
			return true;
		}
		return false;
	}
}