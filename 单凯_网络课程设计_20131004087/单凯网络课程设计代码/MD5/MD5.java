/************************************************
MD5 算法的Java Bean
@author:Topcat Tuppin
Last Modified:10,Mar,2001
 *************************************************/

import java.lang.reflect.*;

/*************************************************
 * md5 类实现了RSA Data Security, Inc.在提交给IETF 的RFC1321中的MD5 message-digest 算法。
 *************************************************/

public class MD5
{
	/*
	 * 下面这些S11-S44实际上是一个4*4的矩阵，在原始的C实现中是用#define 实现的， 这里把它们实现成为static
	 * final是表示了只读，切能在同一个进程空间内的多个 Instance间共享
	 * 
	 * /*md5转换用到的常量，算法本身规定的*/
	static final int S11 = 7;
	static final int S12 = 12;
	static final int S13 = 17;
	static final int S14 = 22;

	static final int S21 = 5;
	static final int S22 = 9;
	static final int S23 = 14;
	static final int S24 = 20;

	static final int S31 = 4;
	static final int S32 = 11;
	static final int S33 = 16;
	static final int S34 = 23;

	static final int S41 = 6;
	static final int S42 = 10;
	static final int S43 = 15;
	static final int S44 = 21;

	/**
	 *用于bits填充的缓冲区，为什么要64个字节呢？因为当欲加密的信息的bits数被512除其余数为448时，
	 *需要填充的bits的最大值为512=64*8 。
	 */
	static final byte[] PADDING =
	{ -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	/*
	 * 下面的三个成员是MD5计算过程中用到的3个核心数据，在原始的C实现中 被定义到MD5_CTX结构中
	 */
	private long[] state = new long[4]; // state (ABCD)
	/*存储原始信息的bits数长度,不包括填充的bits，最长为 2^64 bits，因为2^64是一个64位数的最大值*/
	private long[] count = new long[2]; // number of bits, modulo 2^64 (lsb first)
	/*存放输入的信息的缓冲区，512bits*/
	private byte[] buffer = new byte[64]; // input buffer

	/*
	 * digestHexStr是MD5的唯一一个公共成员，是最新一次计算结果的 　 16进制ASCII表示.
	 */
	public String digestHexStr;

	/*
	 * digest,是最新一次计算结果的2进制内部表示，表示128bit的MD5值.
	 */
	private byte[] digest = new byte[16];

	/*
	 * getMD5ofStr是类MD5最主要的公共方法，入口参数是你想要进行MD5变换的字符串
	 * 返回的是变换完的结果，这个结果是从公共成员digestHexStr取得的．
	 */
	public String getMD5ofStr(String inbuf)
	{
		md5Init();
		md5Update(inbuf.getBytes(), inbuf.length());
		md5Final();
		digestHexStr = "";
		for (int i = 0; i < 16; i++)
		{
			digestHexStr += byteHEX(digest[i]);
		}
		return digestHexStr;

	}

	// 这是MD5这个类的标准构造函数，JavaBean要求有一个public的并且没有参数的构造函数
	public MD5()
	{
		md5Init();

		return;
	}

	/* md5Init是一个初始化函数，初始化核心变量，装入标准的幻数 */
	private void md5Init()
	{
		/*将当前的有效信息的长度设成0,这个很简单,还没有有效信息,长度当然是0了*/
		count[0] = 0L;
		count[1] = 0L;

		/*初始化链接变量，算法要求这样，这个没法解释了*/
		state[0] = 0x67452301L;
		state[1] = 0xefcdab89L;
		state[2] = 0x98badcfeL;
		state[3] = 0x10325476L;

		return;
	}

	/*
	 * F, G, H ,I 是4个基本的MD5函数，在原始的MD5的C实现中，由于它们是
	 * 简单的位运算，可能出于效率的考虑把它们实现成了宏，在java中，我们把它们 　　实现成了private方法，名字保持了原来C中的。
	 */

	private long F(long x, long y, long z)
	{
		return (x & y) | ((~x) & z);

	}

	private long G(long x, long y, long z)
	{
		return (x & z) | (y & (~z));

	}

	private long H(long x, long y, long z)
	{
		return x ^ y ^ z;
	}

	private long I(long x, long y, long z)
	{
		return y ^ (x | (~z));
	}

	/*
	 * FF,GG,HH和II将调用F,G,H,I进行近一步变换 FF, GG, HH, and II transformations for
	 * rounds 1, 2, 3, and 4. Rotation is separate from addition to prevent
	 * recomputation.
	 */

	private long FF(long a, long b, long c, long d, long x, long s, long ac)
	{
		a += F(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private long GG(long a, long b, long c, long d, long x, long s, long ac)
	{
		a += G(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private long HH(long a, long b, long c, long d, long x, long s, long ac)
	{
		a += H(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private long II(long a, long b, long c, long d, long x, long s, long ac)
	{
		a += I(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	/*
	 * md5Update是MD5的主计算过程，inbuf是要变换的字节串，inputlen是长度，这个
	 * 函数由getMD5ofStr调用，调用之前需要调用md5init，因此把它设计成private的
	 * 
	 * 将与加密的信息传递给md5结构，可以多次调用
	 *	input：欲加密的信息，可以任意长
	 *	inputLen：指定input的长度
	 *
	 * MD5 分组更新操作. 继续一个MD5操作,处理另一个消息分组并更新context.
	 */
	private void md5Update(byte[] inbuf, int inputLen)
	{
		int i, index, partLen;
		byte[] block = new byte[64];
		/*计算已有信息的bits长度的字节数的模64, 64bytes=512bits。
		用于判断已有信息加上当前传过来的信息的总长度能不能达到512bits，
		如果能够达到则对凑够的512bits进行一次处理*/
		index = (int) (count[0] >>> 3) & 0x3F;
		//*更新已有信息的bits长度*/
		if ((count[0] += (inputLen << 3)) < (inputLen << 3))
			count[1]++;
		count[1] += (inputLen >>> 29);

		/*计算已有的字节数长度还差多少字节可以 凑成64的整倍数*/
		partLen = 64 - index;

		/*如果当前输入的字节数 大于 已有字节数长度补足64字节整倍数所差的字节数*/
		if (inputLen >= partLen)
		{
			/*用当前输入的内容把buffer的内容补足512bits*/
			md5Memcpy(buffer, inbuf, index, 0, partLen);
			/*用基本函数对填充满的512bits（已经保存到context->buffer中） 做一次转换，
			 * 转换结果保存到context->state中*/
			md5Transform(buffer);

			/*
			对当前输入的剩余字节做转换（如果剩余的字节<在输入的input缓冲区中>大于512bits的话 ），
			转换结果保存到context->state中
			*/
			///*把i+63<inputlen改为i+64<=inputlen更容易理解*/
			for (i = partLen; i + 63 < inputLen; i += 64)
			{
				md5Memcpy(block, inbuf, 0, i, 64);
				md5Transform(block);
			}
			index = 0;
		}
		else
		{
			i = 0;
		}
		/*将输入缓冲区中的不足填充满512bits的剩余内容填充到buffer中，留待以后再作处理*/
		md5Memcpy(buffer, inbuf, index, i, inputLen - i);

	}

	/*
	 * md5Final整理和填写输出结果
	 * /*获取加密 的最终结果
	 * digest：保存最终的加密串
	 */
	private void md5Final()
	{
		byte[] bits = new byte[8];
		int index, padLen;

		/*将要被转换的信息(所有的)的bits长度拷贝到bits中*/
		Encode(bits, count, 8);

		/* 计算所有的bits长度的字节数的模64, 64bytes=512bits*/
		index = (int) (count[0] >>> 3) & 0x3f;
		/*计算需要填充的字节数，padLen的取值范围在1-64之间*/
		padLen = (index < 56) ? (56 - index) : (120 - index);
		/*这一次函数调用绝对不会再导致MD5Transform的被调用，因为这一次不会填满512bits*/
		md5Update(PADDING, padLen);

		/*补上原始信息的bits长度（bits长度固定的用64bits表示），这一次能够恰巧凑够512bits，不会多也不会少*/
		md5Update(bits, 8);

		/*将最终的结果保存到digest中。ok，终于大功告成了*/
		Encode(digest, state, 16);

	}

	/*
	 * md5Memcpy是一个内部使用的byte数组的块拷贝函数，从input的inpos开始把len长度的 　　　　　
	 * 字节拷贝到output的outpos位置开始
	 */

	private void md5Memcpy(byte[] output, byte[] input, int outpos, int inpos,
			int len)
	{
		int i;

		for (i = 0; i < len; i++)
			output[outpos + i] = input[inpos + i];
	}

	/*
	 * md5Transform是MD5核心变换程序，有md5Update调用，block是分块的原始字节
	 * 
	 * 对512bits信息(即block缓冲区)进行一次处理，每次处理包括四轮
		*state[4]：md5结构中的state[4]，用于保存对512bits信息加密的中间结果或者最终结果
		*block[64]：欲加密的512bits信息
	 */
	private void md5Transform(byte block[])
	{
		long a = state[0], b = state[1], c = state[2], d = state[3];
		long[] x = new long[16];

		Decode(x, block, 64);

		/* Round 1 */
		a = FF(a, b, c, d, x[0], S11, 0xd76aa478L); /* 1 */
		d = FF(d, a, b, c, x[1], S12, 0xe8c7b756L); /* 2 */
		c = FF(c, d, a, b, x[2], S13, 0x242070dbL); /* 3 */
		b = FF(b, c, d, a, x[3], S14, 0xc1bdceeeL); /* 4 */
		a = FF(a, b, c, d, x[4], S11, 0xf57c0fafL); /* 5 */
		d = FF(d, a, b, c, x[5], S12, 0x4787c62aL); /* 6 */
		c = FF(c, d, a, b, x[6], S13, 0xa8304613L); /* 7 */
		b = FF(b, c, d, a, x[7], S14, 0xfd469501L); /* 8 */
		a = FF(a, b, c, d, x[8], S11, 0x698098d8L); /* 9 */
		d = FF(d, a, b, c, x[9], S12, 0x8b44f7afL); /* 10 */
		c = FF(c, d, a, b, x[10], S13, 0xffff5bb1L); /* 11 */
		b = FF(b, c, d, a, x[11], S14, 0x895cd7beL); /* 12 */
		a = FF(a, b, c, d, x[12], S11, 0x6b901122L); /* 13 */
		d = FF(d, a, b, c, x[13], S12, 0xfd987193L); /* 14 */
		c = FF(c, d, a, b, x[14], S13, 0xa679438eL); /* 15 */
		b = FF(b, c, d, a, x[15], S14, 0x49b40821L); /* 16 */

		/* Round 2 */
		a = GG(a, b, c, d, x[1], S21, 0xf61e2562L); /* 17 */
		d = GG(d, a, b, c, x[6], S22, 0xc040b340L); /* 18 */
		c = GG(c, d, a, b, x[11], S23, 0x265e5a51L); /* 19 */
		b = GG(b, c, d, a, x[0], S24, 0xe9b6c7aaL); /* 20 */
		a = GG(a, b, c, d, x[5], S21, 0xd62f105dL); /* 21 */
		d = GG(d, a, b, c, x[10], S22, 0x2441453L); /* 22 */
		c = GG(c, d, a, b, x[15], S23, 0xd8a1e681L); /* 23 */
		b = GG(b, c, d, a, x[4], S24, 0xe7d3fbc8L); /* 24 */
		a = GG(a, b, c, d, x[9], S21, 0x21e1cde6L); /* 25 */
		d = GG(d, a, b, c, x[14], S22, 0xc33707d6L); /* 26 */
		c = GG(c, d, a, b, x[3], S23, 0xf4d50d87L); /* 27 */
		b = GG(b, c, d, a, x[8], S24, 0x455a14edL); /* 28 */
		a = GG(a, b, c, d, x[13], S21, 0xa9e3e905L); /* 29 */
		d = GG(d, a, b, c, x[2], S22, 0xfcefa3f8L); /* 30 */
		c = GG(c, d, a, b, x[7], S23, 0x676f02d9L); /* 31 */
		b = GG(b, c, d, a, x[12], S24, 0x8d2a4c8aL); /* 32 */

		/* Round 3 */
		a = HH(a, b, c, d, x[5], S31, 0xfffa3942L); /* 33 */
		d = HH(d, a, b, c, x[8], S32, 0x8771f681L); /* 34 */
		c = HH(c, d, a, b, x[11], S33, 0x6d9d6122L); /* 35 */
		b = HH(b, c, d, a, x[14], S34, 0xfde5380cL); /* 36 */
		a = HH(a, b, c, d, x[1], S31, 0xa4beea44L); /* 37 */
		d = HH(d, a, b, c, x[4], S32, 0x4bdecfa9L); /* 38 */
		c = HH(c, d, a, b, x[7], S33, 0xf6bb4b60L); /* 39 */
		b = HH(b, c, d, a, x[10], S34, 0xbebfbc70L); /* 40 */
		a = HH(a, b, c, d, x[13], S31, 0x289b7ec6L); /* 41 */
		d = HH(d, a, b, c, x[0], S32, 0xeaa127faL); /* 42 */
		c = HH(c, d, a, b, x[3], S33, 0xd4ef3085L); /* 43 */
		b = HH(b, c, d, a, x[6], S34, 0x4881d05L); /* 44 */
		a = HH(a, b, c, d, x[9], S31, 0xd9d4d039L); /* 45 */
		d = HH(d, a, b, c, x[12], S32, 0xe6db99e5L); /* 46 */
		c = HH(c, d, a, b, x[15], S33, 0x1fa27cf8L); /* 47 */
		b = HH(b, c, d, a, x[2], S34, 0xc4ac5665L); /* 48 */

		/* Round 4 */
		a = II(a, b, c, d, x[0], S41, 0xf4292244L); /* 49 */
		d = II(d, a, b, c, x[7], S42, 0x432aff97L); /* 50 */
		c = II(c, d, a, b, x[14], S43, 0xab9423a7L); /* 51 */
		b = II(b, c, d, a, x[5], S44, 0xfc93a039L); /* 52 */
		a = II(a, b, c, d, x[12], S41, 0x655b59c3L); /* 53 */
		d = II(d, a, b, c, x[3], S42, 0x8f0ccc92L); /* 54 */
		c = II(c, d, a, b, x[10], S43, 0xffeff47dL); /* 55 */
		b = II(b, c, d, a, x[1], S44, 0x85845dd1L); /* 56 */
		a = II(a, b, c, d, x[8], S41, 0x6fa87e4fL); /* 57 */
		d = II(d, a, b, c, x[15], S42, 0xfe2ce6e0L); /* 58 */
		c = II(c, d, a, b, x[6], S43, 0xa3014314L); /* 59 */
		b = II(b, c, d, a, x[13], S44, 0x4e0811a1L); /* 60 */
		a = II(a, b, c, d, x[4], S41, 0xf7537e82L); /* 61 */
		d = II(d, a, b, c, x[11], S42, 0xbd3af235L); /* 62 */
		c = II(c, d, a, b, x[2], S43, 0x2ad7d2bbL); /* 63 */
		b = II(b, c, d, a, x[9], S44, 0xeb86d391L); /* 64 */

		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;

	}

	/*
	 * Encode把long数组按顺序拆成byte数组，因为java的long类型是64bit的， 只拆低32bit，以适应原始C实现的用途
	 * 
	 * 将4字节的整数copy到字符形式的缓冲区中
		output：用于输出的字符缓冲区
		input：欲转换的四字节的整数形式的数组
		len：output缓冲区的长度，要求是4的整数倍
*/
	private void Encode(byte[] output, long[] input, int len)
	{
		int i, j;
		for (i = 0, j = 0; j < len; i++, j += 4)
		{
			output[j] = (byte) (input[i] & 0xffL);
			output[j + 1] = (byte) ((input[i] >>> 8) & 0xffL);
			output[j + 2] = (byte) ((input[i] >>> 16) & 0xffL);
			output[j + 3] = (byte) ((input[i] >>> 24) & 0xffL);
		}
	}

	/*
	 * Decode把byte数组按顺序合成成long数组，因为java的long类型是64bit的，
	 * 只合成低32bit，高32bit清零，以适应原始C实现的用途
	 * 
	 * 与上面的函数正好相反，这一个把字符形式的缓冲区中的数据copy到4字节的整数中（即以整数形式保存）
	 * output：保存转换出的整数
	 * input：欲转换的字符缓冲区
	 * len：输入的字符缓冲区的长度，要求是4的整数倍
	 */
	private void Decode(long[] output, byte[] input, int len)
	{
		int i, j;
		for (i = 0, j = 0; j < len; i++, j += 4)
			output[i] = b2iu(input[j]) | (b2iu(input[j + 1]) << 8)
					| (b2iu(input[j + 2]) << 16) | (b2iu(input[j + 3]) << 24);
		return;
	}

	/*
	 * b2iu是我写的一个把byte按照不考虑正负号的原则的＂升位＂程序，因为java没有unsigned运算
	 */
	public static long b2iu(byte b)
	{
		return b < 0 ? b & 0x7F + 128 : b;
	}

	/*
	 * byteHEX()，用来把一个byte类型的数转换成十六进制的ASCII表示，
	 * 　因为java中的byte的toString无法实现这一点，我们又没有C语言中的 sprintf(outbuf,"%02X",ib)
	 */
	public static String byteHEX(byte ib)
	{
		char[] Digit =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	public static void main(String args[])
	{
		MD5 m = new MD5();
		// 如果没有参数，执行标准的Test Suite
		if (Array.getLength(args) == 0)
		{
			System.out.println("MD5 Test suite:");
//			System.out.println("MD5(\"\"):" + m.getMD5ofStr(""));
			System.out.println("MD5(\"a\"):" + m.getMD5ofStr("a"));
//			System.out.println("MD5(\"abc\"):" + m.getMD5ofStr("abc"));
//			System.out.println("MD5(\"message digest\"):"
//					+ m.getMD5ofStr("message digest"));
//			System.out.println("MD5(\"abcdefghijklmnopqrstuvwxyz\"):"
//					+ m.getMD5ofStr("abcdefghijklmnopqrstuvwxyz"));
//			System.out
//					.println("MD5(\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"):"
//							+ m.getMD5ofStr("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"));
		} 
		else 
			System.out.println("MD5(" + args[0] + ")=" + m.getMD5ofStr(args[0]));
	}

}
