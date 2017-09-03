// TypeConverterTest.groovy
// (C) 2016 Masato Kokubo

package org.lightsleep.spec.helper

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.DayOfWeek
import java.util.function.Function
import java.util.concurrent.ConcurrentHashMap

import org.debugtrace.DebugTrace
import org.lightsleep.helper.*

import spock.lang.*

// TypeConverterSpec
@Unroll
class TypeConverterSpec extends Specification {
	enum Size {
	//   0  1  2  3   4
		XS, S, M, L, XL

		static sizeMap = [
			(XS.ordinal()): XS,
			(S .ordinal()): S,
			(M .ordinal()): M,
			(L .ordinal()): L,
			(XL.ordinal()): XL
		]

		static Size valueOf(int ordinal) {
			return sizeMap.get(ordinal)
		}

		static int lastOrdinal() {
			return XL.ordinal()
		}
	}

	@Shared map = new ConcurrentHashMap<>(TypeConverter.typeConverterMap())

	def setupSpec() {
		// Integer -> Size
		TypeConverter.put(map,
			new TypeConverter<>(Integer, Size, {object ->
				Size size = Size.valueOf(object)
				if (size == null)
					throw new ConvertException(Integer, object, Size)
				return size
			})
		)

		// Byte -> Size
		TypeConverter.put(map,
			new TypeConverter<>(Byte, Size,
				TypeConverter.get(map, Byte, Integer).function()
				.andThen(TypeConverter.get(map, Integer, Size).function())
			)
		)

		// Short -> Size
		TypeConverter.put(map,
			new TypeConverter<>(Short, Size,
				TypeConverter.get(map, Short, Integer).function()
				.andThen(TypeConverter.get(map, Integer, Size).function())
			)
		)

		// Long -> Size
		TypeConverter.put(map,
			new TypeConverter<>(Long, Size,
				TypeConverter.get(map, Long, Integer).function()
				.andThen(TypeConverter.get(map, Integer, Size).function())
			)
		)
	}

	// key
	def "TypeConverterSpec key"() {
		when: TypeConverter.key(null, null)
		then: thrown NullPointerException

		when: TypeConverter.key(String, null)
		then: thrown NullPointerException

	}

	// put
	def "TypeConverterSpec put"() {
		when: TypeConverter.put(null, null)
		then: thrown NullPointerException

		when: TypeConverter.put(map, (TypeConverter<?, ?>)null)
		then: thrown NullPointerException

		when: TypeConverter.put(map, TypeConverter.get(map, String, Integer))
		then: noExceptionThrown()
	}

	// get
	def "TypeConverterSpec get"() {
		when: TypeConverter.get(null, null, null)
		then: thrown NullPointerException

		expect:
			TypeConverter.get(map, Cloneable, Iterable) == null
			TypeConverter.get(map, String, Integer).sourceType() == String
			TypeConverter.get(map, String, Integer).destinType() == Integer
			TypeConverter.get(map, String, Integer).key() == 'String->Integer'
	}

	// equals
	def "TypeConverterSpec equals"() {
		expect:
			TypeConverter.get(map, String, Integer) != 'AAA'
			TypeConverter.get(map, String, Integer) != TypeConverter.get(map, Long, Integer)
			TypeConverter.get(map, String, Integer) != TypeConverter.get(map, String, Long)
			TypeConverter.get(map, String, Integer) == TypeConverter.get(map, String, Integer)
			TypeConverter.get(map, String, Integer).hashCode() == TypeConverter.get(map, String, Integer).hashCode()
	}

	// convert
	def "TypeConverterSpec convert"() {
		expect:
			TypeConverter.convert(map, null, Integer) == null
			TypeConverter.convert(map, 1, Integer) == 1

		when: TypeConverter.convert(map, '1', Date)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)
		
		when: TypeConverter.convert(map, 1, int)
		then: e = thrown IllegalArgumentException
		/**/DebugTrace.print('e', e)
	}

	// constructor
	def "TypeConverterSpec constructor"() {
		when: new TypeConverter<>(String, Integer, (Function<String, Integer>)null)
		then: thrown NullPointerException
	}

	// Byte -> Boolean
	def "TypeConverterSpec Byte -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, (byte)-1, Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, (byte)0, Boolean) == false
			TypeConverter.convert(map, (byte)1, Boolean) == true

		when: TypeConverter.convert(map, (byte)2, Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Short -> Boolean
	def "TypeConverterSpec Short -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, (short)-1, Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, (short)0, Boolean) == false
			TypeConverter.convert(map, (short)1, Boolean) == true

		when: TypeConverter.convert(map, (short)2, Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Integer -> Boolean
	def "TypeConverterSpec Integer -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1, Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, 0, Boolean) == false
			TypeConverter.convert(map, 1, Boolean) == true

		when: TypeConverter.convert(map, 2, Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Long -> Boolean
	def "TypeConverterSpec Long -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1L, Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, 0L, Boolean) == false
			TypeConverter.convert(map, 1L, Boolean) == true

		when: TypeConverter.convert(map, 2L, Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Float -> Boolean
	def "TypeConverterSpec Float -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1.0F, Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, 0.0F, Boolean) == false
			TypeConverter.convert(map, 1.0F, Boolean) == true

		when: TypeConverter.convert(map, 2.0F, Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Double -> Boolean
	def "TypeConverterSpec Double -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1.0D, Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, 0.0D, Boolean) == false
			TypeConverter.convert(map, 1.0D, Boolean) == true

		when: TypeConverter.convert(map, 2.0D, Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// BigDecimal -> Boolean
	def "TypeConverterSpec BigDecimal -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, BigDecimal.valueOf(-1L), Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

			TypeConverter.convert(map, BigDecimal.valueOf(0L), Boolean) == false
			TypeConverter.convert(map, BigDecimal.valueOf(1L), Boolean) == true

		when: TypeConverter.convert(map, BigDecimal.valueOf(2L), Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Character -> Boolean
	def "TypeConverterSpec Character -> Boolean"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (char)'0', Boolean) == false
			TypeConverter.convert(map, (char)'1', Boolean) == true

		when: TypeConverter.convert(map, (char)'2', Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// String -> Boolean
	def "TypeConverterSpec String -> Boolean"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Boolean)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '0', Boolean) == false
			TypeConverter.convert(map, '1', Boolean) == true

		when: TypeConverter.convert(map, '2', Boolean)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Boolean -> Byte
	def "TypeConverterSpec Boolean -> Byte"() {
	/**/DebugTrace.enter()
			TypeConverter.convert(map, false, Byte) == (byte)0
			TypeConverter.convert(map, true , Byte) == (byte)1
	/**/DebugTrace.leave()
	}

	// Short -> Byte
	def "TypeConverterSpec Short -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, (short)-129, Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, (short)-128, Byte) == (byte)-128
			TypeConverter.convert(map, (short) 127, Byte) == (byte) 127

		when: TypeConverter.convert(map, (short)128, Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Integer -> Byte
	def "TypeConverterSpec Integer -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -129, Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -128, Byte) == (byte)-128
			TypeConverter.convert(map,  127, Byte) == (byte) 127

		when: TypeConverter.convert(map, 128, Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Long -> Byte
	def "TypeConverterSpec Long -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -129L, Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -128L, Byte) == (byte)-128
			TypeConverter.convert(map,  127L, Byte) == (byte) 127

		when: TypeConverter.convert(map, 128L, Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Float -> Byte
	def "TypeConverterSpec Float -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -129.0F, Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -128.0F, Byte) == (byte)-128
			TypeConverter.convert(map,  127.0F, Byte) == (byte) 127

		when: TypeConverter.convert(map, 128.0F, Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Double -> Byte
	def "TypeConverterSpec Double -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -129.0D, Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -128.0D, Byte) == (byte)-128
			TypeConverter.convert(map,  127.0D, Byte) == (byte) 127

		when: TypeConverter.convert(map, 128.0D, Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// BigDecimal -> Byte
	def "TypeConverterSpec BigDecimal -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, BigDecimal.valueOf(-129L), Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, BigDecimal.valueOf(-128L), Byte) == (byte)-128
			TypeConverter.convert(map, BigDecimal.valueOf( 127L), Byte) == (byte) 127

		when: TypeConverter.convert(map, BigDecimal.valueOf(128L), Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Character -> Byte
	def "TypeConverterSpec Character -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, (char)'\uFF7F', Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, (char)'\uFF80', Byte) == (byte)-128
			TypeConverter.convert(map, (char)'\u007F', Byte) == (byte) 127

		when: TypeConverter.convert(map, (char)'\u0080', Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// String -> Byte
	def "TypeConverterSpec String -> Byte"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Byte)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		when: TypeConverter.convert(map, '-129', Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '-128', Byte) == (byte)-128
			TypeConverter.convert(map,  '127', Byte) == (byte) 127

		when: TypeConverter.convert(map, '128', Byte)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Boolean -> Short
	def "TypeConverterSpec Boolean -> Short"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, Short) == (short)0
			TypeConverter.convert(map, true , Short) == (short)1
	/**/DebugTrace.leave()
	}

	// Byte -> Short
	def "TypeConverterSpec Byte -> Short"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, Short) == (short)-128
			TypeConverter.convert(map, (byte) 127, Short) == (short) 127
	/**/DebugTrace.leave()
	}

	// Integer -> Short
	def "TypeConverterSpec Integer -> Short"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -32769, Short)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -32768, Short) == (short)-32768
			TypeConverter.convert(map,  32767, Short) == (short) 32767

		when: TypeConverter.convert(map, 32768, Short)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Long -> Short
	def "TypeConverterSpec Long -> Short"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -32769L, Short)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -32768L, Short) == (short)-32768
			TypeConverter.convert(map,  32767L, Short) == (short) 32767

		when: TypeConverter.convert(map, 32768L, Short)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Float -> Short
	def "TypeConverterSpec Float -> Short"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -32769.0F, Short)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -32768.0F, Short) == (short)-32768
			TypeConverter.convert(map,  32767.0F, Short) == (short) 32767

		when: TypeConverter.convert(map, 32768.0F, Short)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Double -> Short
	def "TypeConverterSpec Double -> Short"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -32769.0D, Short)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -32768.0D, Short) == (short)-32768
			TypeConverter.convert(map,  32767.0D, Short) == (short) 32767

		when: TypeConverter.convert(map, 32768.0D, Short)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	def "TypeConverterSpec BigDecimal -> Short"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, BigDecimal.valueOf(-32769L), Short)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, BigDecimal.valueOf(-32768L), Short) == (short)-32768
			TypeConverter.convert(map, BigDecimal.valueOf( 32767L), Short) == (short) 32767

		when: TypeConverter.convert(map, BigDecimal.valueOf(32768L), Short)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Character -> Short
	def "TypeConverterSpec Character -> Short"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (char)'\u8000', Short) == (short)-32768
			TypeConverter.convert(map, (char)'\u7FFF', Short) == (short) 32767
	/**/DebugTrace.leave()
	}

	// String -> Short
	def "TypeConverterSpec String -> Short"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Short)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		when: TypeConverter.convert(map, '-32769', Short)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '-32768', Short) == (short)-32768
			TypeConverter.convert(map,  '32767', Short) == (short) 32767

		when: TypeConverter.convert(map, '32768', Short)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Boolean -> Integer
	def "TypeConverterSpec Boolean -> Integer"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, Integer) == 0
			TypeConverter.convert(map, true , Integer) == 1
	/**/DebugTrace.leave()
	}

	// Byte -> Integer
	def "TypeConverterSpec Byte -> Integer"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, Integer) == -128
			TypeConverter.convert(map, (byte) 127, Integer) ==  127
	/**/DebugTrace.leave()
	}

	// Short -> Integer
	def "TypeConverterSpec Short -> Integer"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (short)-32768, Integer) == -32768
			TypeConverter.convert(map, (short) 32767, Integer) ==  32767
	/**/DebugTrace.leave()
	}

	// Long -> Integer
	def "TypeConverterSpec Long -> Integer"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -2147483649L, Integer)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -2147483648L, Integer) == -2147483648
			TypeConverter.convert(map,  2147483647L, Integer) ==  2147483647

		when: TypeConverter.convert(map, 2147483648L, Integer)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Float -> Integer
	def "TypeConverterSpec Float -> Integer"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -2147484000.0F, Integer)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -2147483648.0F, Integer) == -2147483648
			TypeConverter.convert(map,  2147483647.0F, Integer) ==  2147483647

		when: TypeConverter.convert(map, 2147484000.0F, Integer)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Double -> Integer
	def "TypeConverterSpec Double -> Integer"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -2147483649.0D, Integer)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -2147483648.0D, Integer) == -2147483648
			TypeConverter.convert(map,  2147483647.0D, Integer) ==  2147483647

		when: TypeConverter.convert(map, 2147483648.0D, Integer)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// BigDecimal -> Integer
	def "TypeConverterSpec BigDecimal -> Integer"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, BigDecimal.valueOf(-2147483649L), Integer)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, BigDecimal.valueOf(-2147483648L), Integer) == -2147483648
			TypeConverter.convert(map, BigDecimal.valueOf( 2147483647L), Integer) ==  2147483647

		when: TypeConverter.convert(map, BigDecimal.valueOf(2147483648L), Integer)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Character -> Integer
	def "TypeConverterSpec Character -> Integer"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (char)'\u0000', Integer) ==     0
			TypeConverter.convert(map, (char)'\u7FFF', Integer) == 32767
	/**/DebugTrace.leave()
	}

	// String -> Integer
	def "TypeConverterSpec String -> Integer"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Integer)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		when: TypeConverter.convert(map, '-2147483649', Integer)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

			TypeConverter.convert(map, '-2147483648', Integer) == -2147483648
			TypeConverter.convert(map,  '2147483647', Integer) ==  2147483647

		when: TypeConverter.convert(map, '2147483648', Integer)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// java.util.Date -> Integer
	def "TypeConverterSpec utilDate -> Integer"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new java.util.Date((( 0*60+ 0)*60+ 0)*1000L    ), Integer) == (( 0*60+ 0)*60+ 0)*1000
			TypeConverter.convert(map, new java.util.Date(((23*60+59)*60+59)*1000L+999), Integer) == ((23*60+59)*60+59)*1000+999

			TypeConverter.convert(map, new java.sql.Date ((( 0*60+ 0)*60+ 0)*1000L    ), Integer) == (( 0*60+ 0)*60+ 0)*1000
			TypeConverter.convert(map, new java.sql.Date (((23*60+59)*60+59)*1000L+999), Integer) == ((23*60+59)*60+59)*1000+999

			TypeConverter.convert(map, new Time          ((( 0*60+ 0)*60+ 0)*1000L    ), Integer) == (( 0*60+ 0)*60+ 0)*1000
			TypeConverter.convert(map, new Time          (((23*60+59)*60+59)*1000L+999), Integer) == ((23*60+59)*60+59)*1000+999

			TypeConverter.convert(map, new Timestamp     ((( 0*60+ 0)*60+ 0)*1000L    ), Integer) == (( 0*60+ 0)*60+ 0)*1000
			TypeConverter.convert(map, new Timestamp     (((23*60+59)*60+59)*1000L+999), Integer) == ((23*60+59)*60+59)*1000+999
	/**/DebugTrace.leave()
	}

	// Boolean -> Long
	def "TypeConverterSpec Boolean -> Long"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, Long) == 0L
			TypeConverter.convert(map, true , Long) == 1L
	/**/DebugTrace.leave()
	}

	// Byte -> Long
	def "TypeConverterSpec Byte -> Long"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, Long) == -128L
			TypeConverter.convert(map, (byte) 127, Long) ==  127L
	/**/DebugTrace.leave()
	}

	// Short -> Long
	def "TypeConverterSpec Short -> Long"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (short)-32768, Long) == -32768L
			TypeConverter.convert(map, (short) 32767, Long) ==  32767L
	/**/DebugTrace.leave()
	}

	// Integer -> Long
	def "TypeConverterSpec Integer -> Long"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -2147483648, Long) == -2147483648L
			TypeConverter.convert(map,  2147483647, Long) ==  2147483647L
	/**/DebugTrace.leave()
	}

	// Float -> Long
	def "TypeConverterSpec Float -> Long"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -9223373000000000000.0F, Long)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -9223372036854775808.0F, Long) == -9223372036854775808L
			TypeConverter.convert(map,  9223372036854775807.0F, Long) ==  9223372036854775807L

		when: TypeConverter.convert(map, 9223373000000000000.0F, Long)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Double -> Long
	def "TypeConverterSpec Double -> Long"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -9223372036854780000.0D, Long)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, -9223372036854775808.0D, Long) == -9223372036854775808L
			TypeConverter.convert(map,  9223372036854775807.0D, Long) ==  9223372036854775807L

		when: TypeConverter.convert(map, 9223372036854780000.0D, Long)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// BigDecimal -> Long
	def "TypeConverterSpec BigDecimal -> Long"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, new BigDecimal('-9223372036854775809'), Long)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, BigDecimal.valueOf(-9223372036854775808L), Long) == -9223372036854775808L
			TypeConverter.convert(map, BigDecimal.valueOf( 9223372036854775807L), Long) ==  9223372036854775807L

		when: TypeConverter.convert(map, new BigDecimal('9223372036854775808'), Long)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Character -> Long
	def "TypeConverterSpec Character -> Long"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (char)'\u0000', Long) ==     0L
			TypeConverter.convert(map, (char)'\u7FFF', Long) == 32767L
	/**/DebugTrace.leave()
	}

	// String -> Long
	def "TypeConverterSpec String -> Long"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Long)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		when: TypeConverter.convert(map, '-9223372036854775809', Long)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '-9223372036854775808', Long) == -9223372036854775808L
			TypeConverter.convert(map,  '9223372036854775807', Long) ==  9223372036854775807L

		when: TypeConverter.convert(map, '9223372036854775808', Long)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// java.util.Date -> Long
	def "TypeConverterSpec utilDate -> Long"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new java.util.Date((( 0*60+ 0)*60+ 0)*1000L    ), Long) == (( 0*60+ 0)*60+ 0)*1000L
			TypeConverter.convert(map, new java.util.Date(((23*60+59)*60+59)*1000L+999), Long) == ((23*60+59)*60+59)*1000L+999

			TypeConverter.convert(map, new java.sql.Date ((( 0*60+ 0)*60+ 0)*1000L    ), Long) == (( 0*60+ 0)*60+ 0)*1000L
			TypeConverter.convert(map, new java.sql.Date (((23*60+59)*60+59)*1000L+999), Long) == ((23*60+59)*60+59)*1000L+999

			TypeConverter.convert(map, new Time          ((( 0*60+ 0)*60+ 0)*1000L    ), Long) == (( 0*60+ 0)*60+ 0)*1000L
			TypeConverter.convert(map, new Time          (((23*60+59)*60+59)*1000L+999), Long) == ((23*60+59)*60+59)*1000L+999

			TypeConverter.convert(map, new Timestamp     ((( 0*60+ 0)*60+ 0)*1000L    ), Long) == (( 0*60+ 0)*60+ 0)*1000L
			TypeConverter.convert(map, new Timestamp     (((23*60+59)*60+59)*1000L+999), Long) == ((23*60+59)*60+59)*1000L+999
	/**/DebugTrace.leave()
	}

	// Boolean -> Float
	def "TypeConverterSpec Boolean -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, Float) == 0.0F
			TypeConverter.convert(map, true , Float) == 1.0F
	/**/DebugTrace.leave()
	}

	// Byte -> Float
	def "TypeConverterSpec Byte -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, Float) == -128.0F
			TypeConverter.convert(map, (byte) 127, Float) ==  127.0F
	/**/DebugTrace.leave()
	}

	// Short -> Float
	def "TypeConverterSpec Short -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (short)-32768, Float) == -32768.0F
			TypeConverter.convert(map, (short) 32767, Float) ==  32767.0F
	/**/DebugTrace.leave()
	}

	// Integer -> Float
	def "TypeConverterSpec Integer -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -2147483000, Float) == -2147483000.0F
			TypeConverter.convert(map,  2147483000, Float) ==  2147483000.0F
	/**/DebugTrace.leave()
	}

	// Long -> Float
	def "TypeConverterSpec Long -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -9223372000000000000L, Float) == -9223372000000000000.0F
			TypeConverter.convert(map,  9223372000000000000L, Float) ==  9223372000000000000.0F
	/**/DebugTrace.leave()
	}

	// Double -> Float
	def "TypeConverterSpec Double -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -1234567.0D, Float) == -1234567.0F
			TypeConverter.convert(map,  1234567.0D, Float) ==  1234567.0F
	/**/DebugTrace.leave()
	}

	// BigDecimal -> Float
	def "TypeConverterSpec BigDecimal -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new BigDecimal('-1234.125'), Float) == -1234.125F
			TypeConverter.convert(map, new BigDecimal( '1234.125'), Float) ==  1234.125F
	/**/DebugTrace.leave()
	}

	// Character -> Float
	def "TypeConverterSpec Character -> Float"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (char)'\u0000', Float) ==     0.0F
			TypeConverter.convert(map, (char)'\u7FFF', Float) == 32767.0F
	/**/DebugTrace.leave()
	}

	// String -> Float
	def "TypeConverterSpec String -> Float"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Float)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '-1234.125', Float) == -1234.125F
			TypeConverter.convert(map,  '1234.125', Float) ==  1234.125F
	/**/DebugTrace.leave()
	}

	// Boolean -> Double
	def "TypeConverterSpec Boolean -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, Double) == 0.0
			TypeConverter.convert(map, true , Double) == 1.0
	/**/DebugTrace.leave()
	}

	// Byte -> Double
	def "TypeConverterSpec Byte -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, Double) == -128.0
			TypeConverter.convert(map, (byte) 127, Double) ==  127.0
	/**/DebugTrace.leave()
	}

	// Short -> Double
	def "TypeConverterSpec Short -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (short)-32768, Double) == -32768.0
			TypeConverter.convert(map, (short) 32767, Double) ==  32767.0
	/**/DebugTrace.leave()
	}

	// Integer -> Double
	def "TypeConverterSpec Integer -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -2147483648, Double) == -2147483648.0
			TypeConverter.convert(map,  2147483647, Double) ==  2147483647.0
	/**/DebugTrace.leave()
	}

	// Long -> Double
	def "TypeConverterSpec Long -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -9223372036854770000L, Double) == -9223372036854770000.0
			TypeConverter.convert(map,  9223372036854770000L, Double) ==  9223372036854770000.0
	/**/DebugTrace.leave()
	}

	// Float -> Double
	def "TypeConverterSpec Float -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -1234567.0F, Double) == -1234567.0
			TypeConverter.convert(map,  1234567.0F, Double) ==  1234567.0
	/**/DebugTrace.leave()
	}

	// BigDecimal -> Double
	def "TypeConverterSpec BigDecimal -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new BigDecimal('-12345678901.0625'), Double) == -12345678901.0625
			TypeConverter.convert(map, new BigDecimal( '12345678901.0625'), Double) ==  12345678901.0625
	/**/DebugTrace.leave()
	}

	// Character -> Double
	def "TypeConverterSpec Character -> Double"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (char)'\u0000', Double) ==     0.0
			TypeConverter.convert(map, (char)'\u7FFF', Double) == 32767.0
	/**/DebugTrace.leave()
	}

	// String -> Double
	def "TypeConverterSpec String -> Double"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Double)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '-12345678901.0625', Double) == -12345678901.0625
			TypeConverter.convert(map,  '12345678901.0625', Double) ==  12345678901.0625
	/**/DebugTrace.leave()
	}

	// Boolean -> BigDecimal
	def "TypeConverterSpec Boolean -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, BigDecimal) == BigDecimal.ZERO
			TypeConverter.convert(map, true , BigDecimal) == BigDecimal.ONE
	/**/DebugTrace.leave()
	}

	// Byte -> BigDecimal
	def "TypeConverterSpec Byte -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, BigDecimal) == BigDecimal.valueOf(-128L)
			TypeConverter.convert(map, (byte) 127, BigDecimal) == BigDecimal.valueOf( 127L)
	/**/DebugTrace.leave()
	}

	// Short -> BigDecimal
	def "TypeConverterSpec Short -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (short)-32768, BigDecimal) == BigDecimal.valueOf(-32768L)
			TypeConverter.convert(map, (short) 32767, BigDecimal) == BigDecimal.valueOf( 32767L)
	/**/DebugTrace.leave()
	}

	// Integer -> BigDecimal
	def "TypeConverterSpec Integer -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -2147483648, BigDecimal) == BigDecimal.valueOf(-2147483648L)
			TypeConverter.convert(map,  2147483647, BigDecimal) == BigDecimal.valueOf( 2147483647L)
	/**/DebugTrace.leave()
	}

	// Long -> BigDecimal
	def "TypeConverterSpec Long -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -9223372036854775808L, BigDecimal) == BigDecimal.valueOf(-9223372036854775808L)
			TypeConverter.convert(map,  9223372036854775807L, BigDecimal) == BigDecimal.valueOf( 9223372036854775807L)
	/**/DebugTrace.leave()
	}

	// Float -> BigDecimal
	def "TypeConverterSpec Float -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -1234.125F, BigDecimal) == new BigDecimal('-1234.125')
			TypeConverter.convert(map,  1234.125F, BigDecimal) == new BigDecimal( '1234.125')
	/**/DebugTrace.leave()
	}

	// Double -> BigDecimal
	def "TypeConverterSpec Double -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -12345678901.0625D, BigDecimal) == new BigDecimal('-12345678901.0625')
			TypeConverter.convert(map,  12345678901.0625D, BigDecimal) == new BigDecimal( '12345678901.0625')
	/**/DebugTrace.leave()
	}

	// Character -> BigDecimal
	def "TypeConverterSpec Character -> BigDecimal"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (char)'\u0000', BigDecimal) == BigDecimal.ZERO
			TypeConverter.convert(map, (char)'\u7FFF', BigDecimal) == BigDecimal.valueOf(32767L)
	/**/DebugTrace.leave()
	}

	// String -> BigDecimal
	def "TypeConverterSpec String -> BigDecimal"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', BigDecimal)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '-12345678901234567890.1234567890', BigDecimal) == new BigDecimal('-12345678901234567890.1234567890')
			TypeConverter.convert(map,  '12345678901234567890.1234567890', BigDecimal) == new BigDecimal( '12345678901234567890.1234567890')
	/**/DebugTrace.leave()
	}

	// Boolean -> Character
	def "TypeConverterSpec Boolean -> Character"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, Character) == (char)'0'
			TypeConverter.convert(map, true , Character) == (char)'1'
	/**/DebugTrace.leave()
	}

	// Byte -> Character
	def "TypeConverterSpec Byte -> Character"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, Character) == (char)'\uFF80'
			TypeConverter.convert(map, (byte) 127, Character) == (char)'\u007F'
	/**/DebugTrace.leave()
	}

	// Short -> Character
	def "TypeConverterSpec Short -> Character"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (short)-32768, Character) == (char)'\u8000'
			TypeConverter.convert(map, (short) 32767, Character) == (char)'\u7FFF'
	/**/DebugTrace.leave()
	}

	// Integer -> Character
	def "TypeConverterSpec Integer -> Character"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1, Character)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map,      0, Character) == (char)'\u0000'
			TypeConverter.convert(map,  32767, Character) == (char)'\u7FFF'

		when: TypeConverter.convert(map, 65536, Character)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Long -> Character
	def "TypeConverterSpec Long -> Character"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1L, Character)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map,    -0L, Character) == (char)'\u0000'
			TypeConverter.convert(map, 32767L, Character) == (char)'\u7FFF'

		when: TypeConverter.convert(map, 65536L, Character)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Float -> Character
	def "TypeConverterSpec Float -> Character"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1.0F, Character)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map,    -0.0F, Character) == (char)'\u0000'
			TypeConverter.convert(map, 32767.0F, Character) == (char)'\u7FFF'

		when: TypeConverter.convert(map, 65536.0F, Character)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Double -> Character
	def "TypeConverterSpec Double -> Character"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, -1.0D, Character)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map,    -0.0D, Character) == (char)'\u0000'
			TypeConverter.convert(map, 32767.0D, Character) == (char)'\u7FFF'

		when: TypeConverter.convert(map, 65536.0D, Character)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// BigDecimal -> Character
	def "TypeConverterSpec BigDecimal -> Character"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, BigDecimal.valueOf(-1L), Character)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, BigDecimal.valueOf(   -0L), Character) == (char)'\u0000'
			TypeConverter.convert(map, BigDecimal.valueOf(32767L), Character) == (char)'\u7FFF'

		when: TypeConverter.convert(map, BigDecimal.valueOf(65536L), Character)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		when: TypeConverter.convert(map, new BigDecimal('0.1'), Character)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

	/**/DebugTrace.leave()
	}

	// String -> Character
	def "TypeConverterSpec String -> Character"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, '', Character)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, '\u0000', Character) == (char)'\u0000'
			TypeConverter.convert(map, '\\'    , Character) == (char)'\\'
			TypeConverter.convert(map, 'A'     , Character) == (char)'A'
			TypeConverter.convert(map, '漢'    , Character) == (char)'漢'

		when: TypeConverter.convert(map, 'AA', Character)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	static class Foo {
		@Override
		public String toString() {return 'aFoo'}
	}

	// Object -> String
	def "TypeConverterSpec Object -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Foo(), String) == 'aFoo'
	/**/DebugTrace.leave()
	}

	// Boolean -> String
	def "TypeConverterSpec Boolean -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, false, String) == 'false'
			TypeConverter.convert(map, true , String) == 'true'
	/**/DebugTrace.leave()
	}

	// Byte -> String
	def "TypeConverterSpec Byte -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (byte)-128, String) == '-128'
			TypeConverter.convert(map, (byte) 127, String) ==  '127'
	/**/DebugTrace.leave()
	}

	// Short -> String
	def "TypeConverterSpec Short -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (short)-32768, String) == '-32768'
			TypeConverter.convert(map, (short) 32767, String) ==  '32767'
	/**/DebugTrace.leave()
	}

	// Integer -> String
	def "TypeConverterSpec Integer -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -2147483648, String) == '-2147483648'
			TypeConverter.convert(map,  2147483647, String) ==  '2147483647'
	/**/DebugTrace.leave()
	}

	// Long -> String
	def "TypeConverterSpec Long -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -9223372036854775808L, String) == '-9223372036854775808'
			TypeConverter.convert(map,  9223372036854775807L, String) ==  '9223372036854775807'
	/**/DebugTrace.leave()
	}

	// Float -> String
	def "TypeConverterSpec Float -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -1234.125F, String) == '-1234.125'
			TypeConverter.convert(map,  1234.125F, String) ==  '1234.125'
	/**/DebugTrace.leave()
	}

	// Double -> String
	def "TypeConverterSpec Double -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, -12345678901.0625D, String) == '-1.23456789010625E10'
			TypeConverter.convert(map,  12345678901.0625D, String) ==  '1.23456789010625E10'
	/**/DebugTrace.leave()
	}

	// BigDecimal -> String
	def "TypeConverterSpec BigDecimal -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new BigDecimal('-12345678901234567890.1234567890'), String) == '-12345678901234567890.1234567890'
			TypeConverter.convert(map, new BigDecimal( '12345678901234567890.1234567890'), String) ==  '12345678901234567890.1234567890'
	/**/DebugTrace.leave()
	}

	// Character -> String
	def "TypeConverterSpec Character -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, '\u0000', String) == '\u0000'
			TypeConverter.convert(map, '\\', String) == '\\'
			TypeConverter.convert(map, 'A' , String) == 'A'
			TypeConverter.convert(map, '漢', String) == '漢'
	/**/DebugTrace.leave()
	}

	// java.util.Date -> String
	def "TypeConverterSpec utilDate -> String"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT-6'))
		then:
			TypeConverter.convert(map, new java.util.Date((( 0*60+ 0)*60+ 0)*1000L), String) == '1969-12-31'
			TypeConverter.convert(map, new java.util.Date(((23*60+59)*60+59)*1000L), String) == '1970-01-01'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, new java.util.Date((( 0*60+ 0)*60+ 0)*1000L), String) == '1970-01-01'
			TypeConverter.convert(map, new java.util.Date(((23*60+59)*60+59)*1000L), String) == '1970-01-01'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT+6'))
		then:
			TypeConverter.convert(map, new java.util.Date((( 0*60+ 0)*60+ 0)*1000L), String) == '1970-01-01'
			TypeConverter.convert(map, new java.util.Date(((23*60+59)*60+59)*1000L), String) == '1970-01-02'

		cleanup:
			TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// java.sql.Date -> String
	def "TypeConverterSpec sqlDate -> String"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT-6'))
		then:
			TypeConverter.convert(map, new Date((( 0*60+ 0)*60+ 0)*1000L), String) == '1969-12-31'
			TypeConverter.convert(map, new Date(((23*60+59)*60+59)*1000L), String) == '1970-01-01'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, new Date((( 0*60+ 0)*60+ 0)*1000L), String) == '1970-01-01'
			TypeConverter.convert(map, new Date(((23*60+59)*60+59)*1000L), String) == '1970-01-01'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT+6'))
		then:
			TypeConverter.convert(map, new Date((( 0*60+ 0)*60+ 0)*1000L), String) == '1970-01-01'
			TypeConverter.convert(map, new Date(((23*60+59)*60+59)*1000L), String) == '1970-01-02'

		cleanup:
			TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// Time -> String
	def "TypeConverterSpec Time -> String"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT-6'))
		then:
			TypeConverter.convert(map, new Time((( 0*60+ 0)*60+ 0)*1000L), String) == '18:00:00'
			TypeConverter.convert(map, new Time(((23*60+59)*60+59)*1000L), String) == '17:59:59'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, new Time((( 0*60+ 0)*60+ 0)*1000L), String) == '00:00:00'
			TypeConverter.convert(map, new Time(((23*60+59)*60+59)*1000L), String) == '23:59:59'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT+6'))
		then:
			TypeConverter.convert(map, new Time((( 0*60+ 0)*60+ 0)*1000L), String) == '06:00:00'
			TypeConverter.convert(map, new Time(((23*60+59)*60+59)*1000L), String) == '05:59:59'

		cleanup:
			TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// Timestamp -> String
	def "TypeConverterSpec Timestamp -> String"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT-6'))
		then:
			TypeConverter.convert(map, new Timestamp((( 0*60+ 0)*60+ 0)*1000L    ), String) == '1969-12-31 18:00:00.000'
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+900), String) == '1970-01-01 17:59:59.900'
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+990), String) == '1970-01-01 17:59:59.990'
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+999), String) == '1970-01-01 17:59:59.999'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, new Timestamp((( 0*60+ 0)*60+ 0)*1000L    ), String) == '1970-01-01 00:00:00.000'
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+999), String) == '1970-01-01 23:59:59.999'

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT+6'))
		then:
			TypeConverter.convert(map, new Timestamp((( 0*60+ 0)*60+ 0)*1000L    ), String) == '1970-01-01 06:00:00.000'
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+999), String) == '1970-01-02 05:59:59.999'

		cleanup:
			TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// Long -> java.util.Date
	def "TypeConverterSpec Long -> java.util.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000L    , java.util.Date) == new java.util.Date((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000L+999, java.util.Date) == new java.util.Date(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Integer -> java.util.Date (since 1.8.0)
	def "TypeConverterSpec Integer -> java.util.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000    , java.util.Date) == new java.util.Date((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000+999, java.util.Date) == new java.util.Date(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// java.sql.Date -> java.util.Date (cast) (since 1.4.0)
	def "TypeConverterSpec sqlDate -> java.util.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Date((( 0*60+ 0)*60+ 0)*1000L    ), java.util.Date) == new Date((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Date(((23*60+59)*60+59)*1000L+999), java.util.Date) == new Date(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Time -> java.util.Date (cast)
	def "TypeConverterSpec Time -> java.util.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Time((( 0*60+ 0)*60+ 0)*1000L    ), java.util.Date) == new Time((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Time(((23*60+59)*60+59)*1000L+999), java.util.Date) == new Time(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Timestamp -> java.util.Date (cast)
	def "TypeConverterSpec Timestamp -> java.util.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Timestamp((( 0*60+ 0)*60+ 0)*1000L    ), java.util.Date) == new Timestamp((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+999), java.util.Date) == new Timestamp(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// String -> java.util.Date
	def "TypeConverterSpec String -> java.util.Date"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, '1969-12-31', java.util.Date) == new java.util.Date(-1*24*60*60*1000L)
			TypeConverter.convert(map, '1970-01-01', java.util.Date) == new java.util.Date( 0*24*60*60*1000L)
			TypeConverter.convert(map, '1970-01-02', java.util.Date) == new java.util.Date( 1*24*60*60*1000L)

		when: TypeConverter.convert(map, '1970-01-XX', java.util.Date)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		cleanup:
			TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// Long -> java.sql.Date (since 1.8.0)
	def "TypeConverterSpec Long -> java.sql.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000L    , Date) == new Date((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000L+999, Date) == new Date(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Integer -> java.sql.Date
	def "TypeConverterSpec Integer -> java.sql.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000    , Date) == new Date((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000+999, Date) == new Date(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Time -> java.sql.Date
	def "TypeConverterSpec Time -> java.sql.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Time((( 0*60+ 0)*60+ 0)*1000L    ), Date) == new Date((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Time(((23*60+59)*60+59)*1000L+999), Date) == new Date(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Timestamp -> java.sql.Date
	def "TypeConverterSpec Timestamp -> java.sql.Date"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Timestamp((( 0*60+ 0)*60+ 0)*1000L    ), Date) == new Date((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+999), Date) == new Date(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// String -> java.sql.Date
	def "TypeConverterSpec String -> java.sql.Date"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, '1969-12-31', Date) == new Date(-1*24*60*60*1000L)
			TypeConverter.convert(map, '1970-01-01', Date) == new Date( 0*24*60*60*1000L)
			TypeConverter.convert(map, '1970-01-02', Date) == new Date( 1*24*60*60*1000L)

		when: TypeConverter.convert(map, '1970-01-XX', Date)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// Long -> Time
	def "TypeConverterSpec Long -> Time"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000L    , Time) == new Time((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000L+999, Time) == new Time(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Integer -> Time (since 1.8.0)
	def "TypeConverterSpec Integer -> Time"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000    , Time) == new Time((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000+999, Time) == new Time(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// java.util.Date -> Time
	def "TypeConverterSpec utilDate -> Time"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new java.util.Date((( 0*60+ 0)*60+ 0)*1000L    ), Time) == new Time((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new java.util.Date(((23*60+59)*60+59)*1000L+999), Time) == new Time(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// java.sql.Date -> Time
	def "TypeConverterSpec sqlDate -> Time"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Date((( 0*60+ 0)*60+ 0)*1000L    ), Time) == new Time((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Date(((23*60+59)*60+59)*1000L+999), Time) == new Time(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Timestamp -> Time
	def "TypeConverterSpec Timestamp -> Time"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Timestamp((( 0*60+ 0)*60+ 0)*1000L    ), Time) == new Time((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Timestamp(((23*60+59)*60+59)*1000L+999), Time) == new Time(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// String -> Time
	def "TypeConverterSpec String -> Time"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, '00:00:00', Time) == new Time((( 0*60+ 0)*60+ 0)*1000L)
			TypeConverter.convert(map, '23:59:59', Time) == new Time(((23*60+59)*60+59)*1000L)

		when: TypeConverter.convert(map, '23:59:AA', Time)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		cleanup:
			TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// Long -> Timestamp
	def "TypeConverterSpec Long -> Timestamp"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000L    , Timestamp) == new Timestamp((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000L+999, Timestamp) == new Timestamp(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Integer -> Timestamp (since 1.8.0)
	def "TypeConverterSpec Integer -> Timestamp"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, (( 0*60+ 0)*60+ 0)*1000    , Timestamp) == new Timestamp((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, ((23*60+59)*60+59)*1000+999, Timestamp) == new Timestamp(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// java.util.Date -> Timestamp
	def "TypeConverterSpec utilDate -> Timestamp"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new java.util.Date((( 0*60+ 0)*60+ 0)*1000L    ), Timestamp) == new Timestamp((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new java.util.Date(((23*60+59)*60+59)*1000L+999), Timestamp) == new Timestamp(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// java.sql.Date -> Timestamp
	def "TypeConverterSpec sqlDate -> Timestamp"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Date((( 0*60+ 0)*60+ 0)*1000L    ), Timestamp) == new Timestamp((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Date(((23*60+59)*60+59)*1000L+999), Timestamp) == new Timestamp(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// Time -> Timestamp
	def "TypeConverterSpec Time -> Timestamp"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, new Time((( 0*60+ 0)*60+ 0)*1000L    ), Timestamp) == new Timestamp((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, new Time(((23*60+59)*60+59)*1000L+999), Timestamp) == new Timestamp(((23*60+59)*60+59)*1000L+999)
	/**/DebugTrace.leave()
	}

	// String -> Timestamp
	def "TypeConverterSpec String -> Timestamp"() {
	/**/DebugTrace.enter()
		setup:
			def timeZone = TimeZone.getDefault()

		when: TimeZone.setDefault(TimeZone.getTimeZone('GMT'))
		then:
			TypeConverter.convert(map, '1970-01-01 00:00:00.000', Timestamp) == new Timestamp((( 0*60+ 0)*60+ 0)*1000L    )
			TypeConverter.convert(map, '1970-01-01 23:59:59.999', Timestamp) == new Timestamp(((23*60+59)*60+59)*1000L+999)

		when: TypeConverter.convert(map, '1970-01-31 00:00:XX', Timestamp)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		cleanup:
			TimeZone.setDefault(timeZone)
	/**/DebugTrace.leave()
	}

	// Enum -> String
	def "TypeConverterSpec Enum -> String"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, Character.UnicodeScript.HIRAGANA, String) == 'HIRAGANA'
			TypeConverter.convert(map, Thread.State.RUNNABLE, String) == 'RUNNABLE'
			TypeConverter.convert(map, DayOfWeek.SUNDAY, String) == 'SUNDAY'
	/**/DebugTrace.leave()
	}

	// Enum -> Byte (since 1.4.0)
	def "TypeConverterSpec Enum -> Byte"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, Size.XS, Byte) == (byte)0
			TypeConverter.convert(map, Size.S , Byte) == (byte)1
			TypeConverter.convert(map, Size.M , Byte) == (byte)2
			TypeConverter.convert(map, Size.L , Byte) == (byte)3
			TypeConverter.convert(map, Size.XL, Byte) == (byte)4
	/**/DebugTrace.leave()
	}

	// Enum -> Short (since 1.4.0)
	def "TypeConverterSpec Enum -> Short"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, Size.XS, Short) == (short)0
			TypeConverter.convert(map, Size.S , Short) == (short)1
			TypeConverter.convert(map, Size.M , Short) == (short)2
			TypeConverter.convert(map, Size.L , Short) == (short)3
			TypeConverter.convert(map, Size.XL, Short) == (short)4
	/**/DebugTrace.leave()
	}

	// Enum -> Integer (since 1.4.0)
	def "TypeConverterSpec Enum -> Integer"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, Size.XS, Integer) == 0
			TypeConverter.convert(map, Size.S , Integer) == 1
			TypeConverter.convert(map, Size.M , Integer) == 2
			TypeConverter.convert(map, Size.L , Integer) == 3
			TypeConverter.convert(map, Size.XL, Integer) == 4
	/**/DebugTrace.leave()
	}

	// Enum -> Long (since 1.4.0)
	def "TypeConverterSpec Enum -> Long"() {
	/**/DebugTrace.enter()
		expect:
			TypeConverter.convert(map, Size.XS, Long) == 0L
			TypeConverter.convert(map, Size.S , Long) == 1L
			TypeConverter.convert(map, Size.M , Long) == 2L
			TypeConverter.convert(map, Size.L , Long) == 3L
			TypeConverter.convert(map, Size.XL, Long) == 4L
	/**/DebugTrace.leave()
	}

	// Byte -> Size (since 1.4.0)
	def "TypeConverterSpec Byte -> Size"() {
	/**/DebugTrace.enter()
		when: TypeConverter.convert(map, (byte)-1, Size)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, (byte)0, Size) == Size.XS
			TypeConverter.convert(map, (byte)1, Size) == Size.S
			TypeConverter.convert(map, (byte)2, Size) == Size.M
			TypeConverter.convert(map, (byte)3, Size) == Size.L
			TypeConverter.convert(map, (byte)4, Size) == Size.XL

		when: TypeConverter.convert(map, (byte)(Size.lastOrdinal() + 1), Size)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)
	/**/DebugTrace.leave()
	}

	// Short -> Size (since 1.4.0)
	def "TypeConverterSpec Short -> Size"() {
	/**/DebugTrace.enter()

		when: TypeConverter.convert(map, (short)-1, Size)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, (short)0, Size) == Size.XS
			TypeConverter.convert(map, (short)1, Size) == Size.S
			TypeConverter.convert(map, (short)2, Size) == Size.M
			TypeConverter.convert(map, (short)3, Size) == Size.L
			TypeConverter.convert(map, (short)4, Size) == Size.XL

		when: TypeConverter.convert(map, (short)(Size.lastOrdinal() + 1), Size)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

	/**/DebugTrace.leave()
	}

	// Integer -> Size (since 1.4.0)
	def "TypeConverterSpec Integer -> Size"() {
	/**/DebugTrace.enter()

		when: TypeConverter.convert(map, -1, Size)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, 0, Size) == Size.XS
			TypeConverter.convert(map, 1, Size) == Size.S
			TypeConverter.convert(map, 2, Size) == Size.M
			TypeConverter.convert(map, 3, Size) == Size.L
			TypeConverter.convert(map, 4, Size) == Size.XL

		when: TypeConverter.convert(map, Size.lastOrdinal() + 1, Size)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

	/**/DebugTrace.leave()
	}

	// Long -> Size (since 1.4.0)
	def "TypeConverterSpec Long -> Size"() {
	/**/DebugTrace.enter()

		when: TypeConverter.convert(map, -1L, Size)
		then: def e = thrown ConvertException
		/**/DebugTrace.print('e', e)

		expect:
			TypeConverter.convert(map, 0L, Size) == Size.XS
			TypeConverter.convert(map, 1L, Size) == Size.S
			TypeConverter.convert(map, 2L, Size) == Size.M
			TypeConverter.convert(map, 3L, Size) == Size.L
			TypeConverter.convert(map, 4L, Size) == Size.XL

		when: TypeConverter.convert(map, Size.lastOrdinal() + 1L, Size)
		then: e = thrown ConvertException
		/**/DebugTrace.print('e', e)

	/**/DebugTrace.leave()
	}

}
