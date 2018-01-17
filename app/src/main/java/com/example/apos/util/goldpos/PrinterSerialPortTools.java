package com.example.apos.util.goldpos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import android.util.Log;


public class PrinterSerialPortTools {

	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;

	public SerialPort getSerialPort(String port, int baudrate) throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			if ((port.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			mSerialPort = new SerialPort(new File(port), baudrate, 0);
		}
		return mSerialPort;
	}

	/**
	 * @param port
	 *            绔彛
	 * @param baudrate
	 *            娉㈢壒鐜�
	 * */
	public PrinterSerialPortTools(String port, int baudrate) {
		try {
			mSerialPort = this.getSerialPort(port, baudrate);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}

	/*void initp() {
		if (mOutputStream != null) {
			try {
				mOutputStream.write(new byte[] { 0x1B, '@' });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	private class ReadThread extends Thread {
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						System.out.println("鎺ユ敹鍒版暟鎹�澶у皬: " + size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/** 鍏抽棴涓插彛 */
	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	protected void destroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		this.closeSerialPort();
		mSerialPort = null;
	}

	// [s] 杈撳嚭
	public void write(String msg) {
		try {
			if (allowToWrite()) {
				if (msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("unicode"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write_gb2312(String msg) {
		try {
			if (allowToWrite()) {
				if (msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("GB2312"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// [s] 杈撳嚭
	public void write_unicode(String msg) {
		try {
			if (allowToWrite()) {
				if (msg == null)
					msg = "";
				mOutputStream.write(msg.getBytes("unicode"));
				mOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// [s] 杈撳嚭
	public void write_Unicode(String msg, boolean test, String Persian) {
		try {

			if (msg == null) {
				msg = "";
			}

			String[] str = new String[msg.length()];

			for (int i = 0; i < msg.length(); i++) {
				str[i] = msg.substring(i, i + 1);
			}
			// 閫夋嫨鐨勬槸鑻辨枃
			if (Persian.equals("English")) {

				// 閫愪釜瀛楃鎵撳嵃锛岃浆鐮佸悗锛岄渶瑕佸皢byte閲岄潰涓や釜瀛楄妭浜掓崲浣嶇疆
				for (int j = 0; j < str.length; j++) {
					byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };
					mOutputStream.write(a);

				}
				// 鎵撳嵃鏈�悗涓�锛屼笉瓒�2浣嶇殑锛岃ˉ绌烘牸锛屾渶鍚庢墦鍗�琛岀┖鏍硷紙绌�琛岋級锛�
				for (int i = 0; i < msg.length() % 32 + 5 * 32; i++) {
					mOutputStream.write(new byte[] { 0x00, 0x20 });
				}
			} else {
				// 娉㈡柉鏂囬『搴忔槸闈犲彸灞呬腑锛屾墍浠ュ厛琛ヤ綑涓嬬殑绌烘牸
				for (int i = 0; i < 32 - msg.length(); i++) {
					mOutputStream.write(new byte[] { 0x00, 0x20 });
				}

				// 閫愪釜鎵撳嵃瀛楃
				ArrayList<String> arraystr = textArrayList(msg); // 寰楀埌鐨勬槸闀垮害涓�2鐨勫瓧绗︿覆闆嗗悎
				ArrayList<String[]> arrayone = new ArrayList<String[]>(); // 闀垮害涓�2鐨勫瓧绗︿覆鏁扮粍闆嗗悎锛岀敤浜庡瓨鏀惧垎鍓插悗鐨勯暱搴︿负32鐨勫瓧绗︿覆闆嗗悎

				for (int i = 0; i < arraystr.size(); i++) {
					System.out.println(arraystr.get(i));
					Log.i("array", arraystr.get(i));
				}
				/**
				 * 鍔犲叆鐨勪唬鐮�鎶婇暱搴︿负32鐨勫瓧绗︿覆锛屽垎鍓叉垚闀垮害涓�2鐨勬暟缁�
				 */
				for (int i = 0; i < arraystr.size(); i++) {
					String strs = arraystr.get(i);
					String[] stry = new String[strs.length()];
					for (int j = 0; j < strs.length(); j++) {
						stry[j] = strs.substring(j, j + 1);
					}
					arrayone.add(stry);

				}

				/**
				 * 杈撳嚭
				 */
				for (int i = 0; i < arrayone.size(); i++) {
					String[] strz = arrayone.get(i);
					int count = 0;
					ArrayList<String> zm = new ArrayList<String>();
					for (int k = 0; k < strz.length; k++) {
						char[] zms = strz[k].toCharArray();
						if ((97 <= zms[0] && zms[0] <= 123) || (65 <= zms[0] && zms[0] <= 91)) {
							zm.add(zms[0] + "");
						}
					}
					// count = zm.size() - 1;
					for (int b = 0; b < zm.size(); b++) {
						System.out.println(zm.get(b));
					}
					for (int j = strz.length - 1; j >= 0; j--) {
						char[] zmss = strz[j].toCharArray();
						if ((97 <= zmss[0] && zmss[0] <= 123) || (65 <= zmss[0] && zmss[0] <= 91)) {
							strz[j] = zm.get(count);
							count++;
							
							byte[] a = { strz[j].getBytes("unicode")[3], strz[j].getBytes("unicode")[2] };
							Log.i("info","-------"+bytesToHexString123(strz[j].getBytes("unicode")));
							mOutputStream.write(a);
						} else {
							byte[] a = { strz[j].getBytes("unicode")[3], strz[j].getBytes("unicode")[2] };
							Log.i("info","-------"+bytesToHexString123(strz[j].getBytes("unicode")));
							mOutputStream.write(a);
						}

					}
				}

				// 鎵撳嵃5琛岀┖鏍�
				for (int i = 0; i < 5 * 32; i++) {
					mOutputStream.write(new byte[] { 0x00, 0x20 });

				}

			}
			mOutputStream.flush();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// [s] 输出 -----阿拉伯语--------
	public void write_Unicode(String msg) {
		// msg =
		// "تأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعةتأكيد عملية الطباعة";
		// msg =
		// "مرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو بايمرحبا بكم في برنامج برونتو باي";
		// msg =
		// "هل تريد القيام بهذه العملية هل تريد القيام بهذه العملية هل تريد القيام بهذه العملية هل تريد القيام بهذه العملية هل تريد القيام بهذه العمليةهل تريد القيام بهذه العملية هل تريد القيام بهذه العملية هل تريد القيام بهذه العملية هل تريد القيام بهذه العملية";
		// msg =
		// " ارحموني و خلصونا من هادي المشكلة  ارحموني و خلصونا من هادي المشكلة  ارحموني و خلصونا من هادي المشكلة  ارحموني و خلصونا من هادي المشكلة  ارحموني و خلصونا من هادي المشكلة  ارحموني و خلصونا من هادي المشكلة  ارحموني و خلصونا من هادي المشكلة  ارحموني و خلصونا من هادي المشكلة";
		// msg =
		// "طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة طباعة تفصيلية و فحص عملية الطباعة";
		ArrayList<String> Arralb = new ArrayList<String>();
		try {
			int duan = msg.length() / 26;

			if (msg.length() % 26 > 0) {
				duan += 1;
			}
			String stralb = null;

			for (int q = 0; q < duan; q++) {

				if (q == duan - 1) {
					String a = msg.substring(26 * q, msg.length() - 1);
					System.out.println(a);
					Arralb.add(a);
				} else {
					stralb = msg.substring(26 * q, 26 * (q + 1));
					System.out.println(stralb);
					Arralb.add(stralb);
				}

			}

			for (int i = 0; i < Arralb.size(); i++) {

				if (Arralb.size() == 1) {
					String slbs = Arralb.get(i);
					ArrayList strs16 = str16(slbs);
					for (int j = 0; j < strs16.size(); j++) {
						int a = (Integer) strs16.get(j);
						mOutputStream.write(a);
					}
				} else {
					String slbs = Arralb.get(i);
					ArrayList strs16 = str16(slbs);
					for (int j = 0; j < strs16.size(); j++) {
						int a = (Integer) strs16.get(j);
						mOutputStream.write(a);
					}
					Thread.sleep(80);
					// if (i != Arralb.size() - 1) {
					mOutputStream.write(new byte[] { 0x0A });
					// }
				}
			}

			mOutputStream.flush();

			if (Arralb.size() == 1) {
				Thread.sleep(75);
				mOutputStream.write(new byte[] { 0x0A });
				for (int i = 0; i < 4 * 32; i++) {
					try {
						mOutputStream.write(new byte[] { 0x00, 0x20 });
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				for (int i = 0; i < 4 * 32; i++) {
					try {
						mOutputStream.write(new byte[] { 0x00, 0x20 });
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 处理阿拉伯语辅助方法
	ArrayList str16(String s) {
		int str16s[] = new int[s.length() + 1];
		int str16sb[] = new int[s.length() + 1];
		ArrayList albstr = new ArrayList();
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			str16s[i] = ch;
		}

		bs bs = new bs();

		boolean zj = false;
		int c = bs.IsIncludeArbic(str16s);
		if (c == 1) {
			zj = true;
		}
		if (zj) {
			bs.Arbic_Convert(str16s, str16sb);
		} else {
			str16sb = str16s;
		}

		for (int i = 0; i < str16sb.length; i++) {

			int b = str16sb[i] / 256;
			albstr.add(b);
			int d = str16sb[i] % 256;
			albstr.add(d);

		}

		for (int j = 0; j < str16s.length; j++) {
			System.out.println(str16s[j]);
		}
		System.out.println("\n================");
		for (int k = 0; k < str16sb.length; k++) {
			System.out.println(str16sb[k]);
		}
		return albstr;
	}

	/**
	 * 鎶婁紶杩涙潵鐨勫瓧绗︿覆鍒嗗壊鎴愰暱搴︿负32鐨勫瓧绗︿覆锛屽啀鏀惧叆ArrayList<String>闆嗗悎杩斿洖銆�
	 * 
	 * @param str
	 * @return
	 */
	public ArrayList<String> textArrayList(String str) {

		System.out.println("瀛楃闀垮害锛� " + str.length());
		int a = str.length() / 32;
		int b = str.length() % 32;
		System.out.println("a = " + a);
		System.out.println("b = " + b);
		String stra = null;
		String strb = null;

		ArrayList<String> cstr = new ArrayList<String>();
		if (a == 1 && str.length() > 32) {
			stra = str.substring(0, 32);
			strb = str.substring(32, str.length());
			if (null != strb) {
				int count = 32 - strb.length();
				for (int i = 0; i < count; i++) {
					strb = strb + " ";
				}
			}
			cstr.add(stra);
			cstr.add(strb);
			System.out.println("==1+1");
		} else if (a > 1) {
			System.out.println("澶т簬1");
			for (int i = 0; i < a; i++) {
				String ca = str.substring(i * 32, (i + 1) * 32);
				cstr.add(ca);
			}
			String cas = str.substring(a * 32, str.length());
			int caslengt = 32 - cas.length();
			for (int i = 0; i < caslengt; i++) {
				cas = cas + " ";
			}
			cstr.add(cas);
		} else {
			System.out.println("==1");
			cstr.add(str);
		}
		System.out.println("闆嗗悎鎵惧害 = " + cstr.size());
		return cstr;
	}

	public void ceshi(String msg) {
		int k = msg.length() / 32;
		int j = msg.length() % 32;
		String[] str = new String[j];
		String[] str32 = new String[32];
		for (int i = 0; i < j; i++) {

			str[i] = msg.substring(k * 32, k * 32 + 1);

		}
		for (int a = k; a > 0; a--) {
			for (int i = 32; i > 0; i--) {
				str32[i] = msg.substring(a * 32, a * 32 + 1);
			}
		}
	}

	private byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();

	}

	// 鍒ゆ柇鏄惁涓鸿嫳鏂囷紱
	private static boolean checkPwdChars(final String str) {
		// 鍏堟鏌ユ渶鍚庝竴浣�鎻愰珮鏁堢巼)
		char tmp;
		int i = str.length() - 1;
		for (; i >= 0; i--) {
			tmp = str.charAt(i);
			if (!(('0' <= tmp && tmp <= '9') || ('a' <= tmp && tmp <= 'z') || ('A' <= tmp && tmp <= 'Z'))) {
				return false;
			}
		}
		;
		return true;
	}

	// 妫�祴鏄惁鏈夌焊
	public boolean getState() {
		try {
			if (allowToWrite()) {

				mOutputStream.write(new byte[] { 0x10, 0x04, 0x05 });
				mOutputStream.flush(); // 1
			}
			Thread.sleep(50);

			int cout = mInputStream.available();
			byte[] buffer = new byte[cout];
			int size = mInputStream.read(buffer);
			if (size == 0) {
				return true;
			}
			if (buffer[0] == 0x00) {
				return true;
			} else
				return false;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static byte[] getByteArray(String hexString) {

		return new BigInteger(hexString, 16).toByteArray();

	}

	public static String bytesToHexString123(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder("");

		if (src == null || src.length <= 0) {
			return null;
		}

		for (int i = 0; i < src.length; i++) {

			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);

			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);

		}
		return stringBuilder.toString();
	}

	/**
	 * 杈撳嚭
	 * */
	public void write(byte[] b) {
		try {
			if (allowToWrite()) {
				if (b == null)
					return;
				mOutputStream.write(b);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 杈撳嚭
	 * */
	public void write(int oneByte) {
		try {
			if (allowToWrite()) {
				mOutputStream.write(oneByte);
				mOutputStream.flush(); // 1
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鏄惁鍏佽鎵撳嵃
	 * */
	public boolean allowToWrite() {
		if (mOutputStream == null) {
			System.out.println("杈撳嚭娴佷负绌� 涓嶈兘鎵撳嵃! ");
			return false;
		}
		return true;
	}

	// [e]

	public void write_GB2312(String msg) {
		if (allowToWrite()) {
			if (msg == null)
				msg = "";

			try {
				mOutputStream.write(msg.getBytes("GB2312"));
				mOutputStream.flush();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 鎵撳嵃鑻辨枃閲嶅仛byte[]
	public static byte[] printerENByte(String msg) {
		byte[] b = msg.getBytes();
		byte[] writebytes = new byte[b.length * 2];
		for (int i = 0; i < b.length; i++) {
			writebytes[i * 2] = 0x00;
			writebytes[i * 2 + 1] = msg.getBytes()[i];
		}
		return writebytes;
	}

	public static String transferString(String oldString) {
		StringBuffer newStringBuffer = new StringBuffer(oldString);

		int length = oldString.length();

		for (int i = 0; i < length / 2 + 1; i++) {
			char a = oldString.charAt(i);
			char b = oldString.charAt(length - i - 1);
			newStringBuffer.replace(i, i + 1, String.valueOf(b));
			newStringBuffer.replace(length - i - 1, length - i, String.valueOf(a));
		}
		return new String(newStringBuffer);
	}

	public static void test1(String str) {
		System.out.println("瀛楃闀垮害锛� " + str.length());
		int a = str.length() / 32;
		int b = str.length() % 32;
		System.out.println("a = " + a);
		System.out.println("b = " + b);

		String stra = str.substring(b, str.length());
		String strb = str.substring(0, b);
		ArrayList<String> cstr = new ArrayList<String>();
		if (a == 1) {

		} else {
			for (int i = 0; i < a; i++) {
				String ca = stra.substring(0, 32);
				stra = stra.substring(32, stra.length());
				cstr.add(ca);
			}
		}

		System.out.println("闆嗗悎鎵惧害 = " + cstr.size());
		if (a > 1) {
			for (int i = cstr.size(); i > 0; i--) {
				System.out.println(cstr.get(i - 1));
			}
			System.out.println(strb);
		} else if (a == 1 && str.length() > 32) {
			System.out.println(stra + "\n" + strb);
		} else {
			System.out.println("111 = " + str);
		}
	}
}
