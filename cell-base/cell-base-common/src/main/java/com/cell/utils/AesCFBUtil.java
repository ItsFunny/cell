package com.cell.utils;

import io.netty.util.CharsetUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class AesCFBUtil
{

	private static byte[] iv = new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };
	private static IvParameterSpec ivSpec = new IvParameterSpec(iv);

	public static byte[] encryptBytes(String encryptKey, byte[] message )throws Exception
    {
		byte[] encryptBuffer = encryptBuffer(encryptKey, message);
		return encryptBuffer;
	}
	public static String encryptString(String encryptKey, String message) throws Exception
    {
		byte[] encryptBuffer = encryptBuffer(encryptKey, message.getBytes(CharsetUtil.UTF_8));
		return Base64Utils.encode(encryptBuffer);
	}

	public static String decryptString(String encryptKey, String encryptMessage) throws Exception
    {
		byte[] buffer = Base64Utils.decode(encryptMessage);
		byte[] decryptBuffer = decryptBuffer(encryptKey, buffer);
		return new String(decryptBuffer, CharsetUtil.UTF_8);
	}

	public static byte[] decryptBytes(String encryptKey, byte[] encryptMessage) throws Exception
    {
		byte[] buffer = encryptMessage;
		byte[] decryptBuffer = decryptBuffer(encryptKey, buffer);
		return decryptBuffer;
	}
	public static byte[] decryptByte(String encryptKey, String encryptMessage) throws Exception
    {
		byte[] buffer = Base64Utils.decode(encryptMessage);
		byte[] decryptBuffer = decryptBuffer(encryptKey, buffer);
		return decryptBuffer;
	}

	public static byte[] encryptBuffer(String encryptKey, byte[] data, int encryptPieceSize) throws Exception
    {
		int len = data.length;
		if (len <= encryptPieceSize) {
			return encryptBuffer(encryptKey, data);
		}
		byte[] encryptData = new byte[len];
		int size = len / encryptPieceSize;
		int start = 0;
		for (int i = 0; i < size; i++) {
			start = encryptPieceSize * i;
			encrypt(start, data, encryptPieceSize, encryptKey, encryptData);
		}

		start += encryptPieceSize;
		int remain = len % encryptPieceSize;
		if (remain != 0) {
			encrypt(start, data, remain, encryptKey, encryptData);
		}
		return encryptData;
	}

	private static void encrypt(int start, byte[] data, int encryptPieceSize, String secret, byte[] encryptData) throws Exception
    {
		byte[] tmp = new byte[encryptPieceSize];
		byte[] subEncryptData = null;
		System.arraycopy(data, start, tmp, 0, encryptPieceSize);
		subEncryptData = encryptBuffer(secret, tmp);
		System.arraycopy(subEncryptData, 0, encryptData, start, encryptPieceSize);
	}

	public static byte[] encryptBuffer(String encryptKey, byte[] buffer) throws Exception
    {
		if (buffer == null) {
			return null;
		}
		SecretKeySpec skeySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(encryptKey.getBytes("utf-8")), "AES");
		Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

		return cipher.doFinal(buffer);
	}

	public static byte[] decryptBuffer(String encryptKey, byte[] encryptData, int encryptPieceSize) throws Exception
    {
		int len = encryptData.length;
		if (len <= encryptPieceSize) {
			return decryptBuffer(encryptKey, encryptData);
		}
		byte[] decryptData = new byte[len];
		int size = len / encryptPieceSize;
		byte[] tmp = new byte[encryptPieceSize];
		byte[] subDecryptData = null;
		int start = 0;
		for (int i = 0; i < size; i++) {
			start = encryptPieceSize * i;
			decrypt(start, encryptData, tmp, encryptPieceSize, subDecryptData, encryptKey, decryptData);
		}

		start += encryptPieceSize;
		int remain = len % encryptPieceSize;
		if (remain != 0) {
			decrypt(start, encryptData, tmp, remain, subDecryptData, encryptKey, decryptData);
		}
		return decryptData;
	}

	private static void decrypt(int start, byte[] data, byte[] tmp, int encryptPieceSize, byte[] subDecryptData, String secret, byte[] decryptData) throws Exception
    {
		System.arraycopy(data, start, tmp, 0, encryptPieceSize);
		subDecryptData = decryptBuffer(secret, tmp);
		System.arraycopy(subDecryptData, 0, decryptData, start, encryptPieceSize);
	}

	public static byte[] decryptBuffer(byte[] key, byte[] buffer) throws Exception
    {
		if (buffer == null) {
			return null;
		}
		
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

		return cipher.doFinal(buffer);
	
	}
	
	public static byte[] decryptBuffer(String encryptKey, byte[] buffer) throws Exception
    {
		if (buffer == null) {
			return null;
		}

		SecretKeySpec skeySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(encryptKey.getBytes("utf-8")), "AES");
		Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

		return cipher.doFinal(buffer);
	}

	static void printBytes( byte [] bytes) {
		System.out.print("{");
		for(int i = 0; i < bytes.length; ++i) {
			System.out.print(bytes[i] + ",");
		}
		System.out.println("}");
	}

	// 与go cfb 解密
	public static byte[] decryptWithGo(String base64Text, String base64Key) throws Exception
	{
		byte[] key=Base64Utils.decode(base64Key);
		byte[] inputArr = Base64Utils.decode(base64Text);
		SecretKeySpec skSpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
		int blockSize = cipher.getBlockSize();
		IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(inputArr, blockSize));
		byte[] dataToDecrypt = Arrays.copyOfRange(inputArr, blockSize, inputArr.length);
		cipher.init(Cipher.DECRYPT_MODE, skSpec, iv);
		byte[] result = cipher.doFinal(dataToDecrypt);
		return result;
	}
	public static void main(String[] args) throws Exception
    {

		byte [] a = {0x1, 0x2, 0x5, 0x4, 0x1, 0x2, 0x5, 0x4, 0x1, 0x2, 0x5, 0x4,0x1, 0x2, 0x5, 0x4};
		byte [] res = encryptBuffer("a", a);
		printBytes(res);
		byte [] b = decryptBuffer("a", res);

		printBytes(b);

//		AtomicInteger i = new AtomicInteger(0);
//
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						String ENCRYPT_KEY = ((Integer)new Random().nextInt()).toString();
//						String str = ((Integer)new Random().nextInt()).toString();
//						byte[] bytes = str.getBytes();
//						byte[] en = encryptBuffer(ENCRYPT_KEY, bytes);
//						byte[] de = decryptBuffer(ENCRYPT_KEY, en);
//						String result = new String(de);
//						if (!result.equals(str)) {
//							System.out.println("Error");
//						}
//
//						i.incrementAndGet();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//
//		new Thread(task).start();
//		new Thread(task).start();
//		new Thread(task).start();
//		new Thread(task).start();
//
//		long time = System.currentTimeMillis();
//		while (true) {
//			if (System.currentTimeMillis() - time >= 1000) {
//				System.out.println(i.getAndSet(0) + "/s");
//				time = System.currentTimeMillis();
//			}
//		}
//
		byte[] data = "123abc123abc123abc123abc123abc123abc123abc123abc123abc123abc123abc123abc123abc123abc123abc".getBytes();
		int encryptPieceSize = 16;
		String encryptKey = UUIDUtil.uuid2();
		byte[] encryptData = encryptBuffer(encryptKey, data, encryptPieceSize);
		System.out.println(Base64Utils.encode(encryptData));
		data = decryptBuffer(encryptKey, encryptData, encryptPieceSize);
		System.out.println(new String(data));
		encryptData = encryptBuffer(encryptKey, data);
		System.out.println(Base64Utils.encode(encryptData));
		data = decryptBuffer(encryptKey, encryptData);
		System.out.println(new String(data));
		
		System.out.println(Base64Utils.decode("8wCCyQo3nr+ctHk5o2DVt/b+jZP2aozMJWoAsjn7cEoVNbd3WuGnIrS5V+USrpb8RvSiPimS6JadFNI52q6twfS9nkeVPKkgmfssvN9I44l9vgmjezYcO16NPcfjyuwu5mY7H2SwEkpA8wXVPKiEyHQwIio4TbVTTKI8Hr3LK1TpzoLjVdgjT5Er1Kkk9ZqkhbcumOWsXKkVsKfNu6qBEGRsFKU528gNljAwQ2poPbnJJ78gkBHXKmbCOBGR2VggYHt8sIs2hoTMm9Iih95TZYoM1UXPUD1XFGJ27KyBLZRXrI3/P0eEU7pmvc/WmvHBM83pQHxtr16C5JJmAgarnjL1xXhbkKHOOn+yJQb/VopKXyfMMruM88aUIYB7wLB78AMYhn83KHD3Zg9YSp1OFs4BADi1Q5kXgOIQbZ1upV3iQ1EQ2ZncDmYi/mhU7SiMp9l0LAxNeBGktHNZoYHGypCdMViRfvm0MGlbu2eoMGPAcL0uDpqiH12J/qYd99TZE+4kT5KAWblmM3eGvY+ThzewNvYoO4NMB2QrsV/lFl8swVz1sVdgNyZhOi8iSs3NjZWrm6FTHFX05JnBvsLNJ/8rbNE18pnMs9VoJWLHcYdQZr2casgfsQ3T2je4KDXTic6q6xsDJTUpcRBRT/QHmud07XLwvA2hGhWftJrXiCsNDBkcd3U5wjgc6wDGuITiAPPRid15SQ8DzX8GrTXAFTs3Ubut2DbM7AHOoVdSZdvqYHlGDJXAVCSoTgKsL6U1VXK8+QFtWKH7bw2EueENUrVBVWVVDT+kzbAX70uz3BkWNVsUccenTkPfsEL+NCr6Yf8GD2VmydKTWHdsjIkmYFm92kFfuouQCWunyvonGa4ACkm8i57ryz+rZOXmfdZncwMfHnXJo4lsaRSzIP6/rTEfIc7+a5kvGYl20ecTLkpBURj/G8fI/2a1PnzolrmA/i0xojIC3FcM6qo937hhoPODtNcSfLgJnEy8Hohpmu2FWo5ilfqa9S2fpoqTjhMazwZ3WXincTHNQ0xVEFulq5s5TPx5paDt9Fnd3gHCSHBycT2gc+hcMmFdTOQ3IoL+UeUhmqCmtVd8CavNKjL3KVHt5hgvBirsbrKuK2NwmnWie/TJ2g60YHi9o2NDsbf3lIA0tBP6XxsWvn+RHp3KVNBuYQ/uw3sBFUog+DioTtBGpCvqdZbu7Sof5dTKy7RMPBIoKK9NDdoz630XKpYRmJ70y4qBYnly5+nGg0VYYTa4zpk/qNjFSxedYElxGK8c9xHwGOIoddxqA+pJ57WRQmoVKeJj3YWqA7q4tuvb+y3mzVE9/883PMjoL2jiN+Fbzc09sVFrfpxKqkVZCqo9Fg==").length);
		System.out.println(MD5Utils.md5("asdasd").getBytes().length);
		
		data = "sad123测".getBytes();
		encryptKey = "abc123";
		byte[] encData = encryptBuffer(encryptKey, "123".getBytes());
		
		System.out.println(Base64Utils.encode(encData));
		System.out.println(Base64Utils.encode(MessageDigest.getInstance("MD5").digest(encryptKey.getBytes())));
		encData = encryptBuffer(encryptKey, data);
		System.out.println(Base64Utils.encode(encData));
		System.out.println(MD5Utils.md5(encryptKey));

		data = decryptBuffer(encryptKey, Base64Utils.decode("BqFiWKld4w=="));
		System.out.println(new String(data));
		
		
		System.out.println(Base64Utils.decode("Eqx8xw0fqIObg+IvBESCGhlxtLz0FhU5xLPOQzvcx5ZTo7rxg4US/hohKHHKcBLr/o1xC4SQzykBOypFbG14RQD1HN+qQv/QHmQ/OMODQpQfTFDSrNk59duALNbAAq9SnAUuN1zCQu9AJ5YS0MBbGpnAfLilpK68FnJl8mC4LSXJG04tvzZNM02cXDS3SDMiotT5XJYd8kq9CDxo33BzzeI/1heuyYBqqReashFh7HoYONguNezZgYlwnVQ/kELCRqws5uGOEfJKZ0CUOuLmAv2voYdi2MEcoi72Xe887JgsexXQfygrtk2DKvWg895l2CXz5WA0XUU/Hc2zBghFvX8lnWeIZKyt3XP1Coy8qsMYuWjsoFMuk2n5oo0s7XPQ/TVvhTCxWXrtZRZN++kd7VUiC7O635AMCWZy8NzGTh2v9P3KeWQravEC8KyM4TX8S5YJcXF4pR1rqW6zEIfU4hes6x2PeJzhW6zFjQCfXoFR/uT9EhDMb09UNigp+bCQ6BC6geCdV5P/U+5/d7PhAernl51/cdNdkNMVPnuyKkMDWuy9LPyrfhe4GFsqa6akJk7STWMOOB2Ni+IPF2h9RRTI1TOBMxnJi/W/0mLpOXhQ2hPr20XVeDNhPcBQWDDfvKrXE2djTtsZaoW5UtB39EcePbK+Uj5MZNBuZcUg2xxoVRwlROduKc+W/3Tsq0dWmspcOa5FmCBxqD39tdWHsSHQfjtKACmyKrniKTECLdPFRill77yCLvlqpDf483bQ6rSp0yy6VN6g720RGgDJw9btLRkV1grBv2o6SwkLF/ADbY8kgvpLsxax+KquMe3V651S6tw+9rKqJFOFGmsueMZevq63fviQ7NvFkiW+i6sYLR0/nMkBuj9/tdd5D7vvbsMj5UpdNrA5WFQY2h5oWaVqWeG1nF3F").length);
		
		byte[] key = new byte[32];
		data = decryptBuffer(key, Base64Utils.decode("+5phdvxFIFqWus6T+7Wu"));
		System.out.println(new String(data));
	}

}
