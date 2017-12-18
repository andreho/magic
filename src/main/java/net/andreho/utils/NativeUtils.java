package net.andreho.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

/**
 * <b>Most of java docs were copied from original sun.misc.Unsafe source code.</b>
 * <br/>Created by a.hofmann on 17.05.2014 at 16:36<br/>
 */
public final class NativeUtils {

  private static final String CLASS_NAME = "sun.misc.Unsafe";
  static final Unsafe UNSAFE;

  static {
    Unsafe u = null;

    try {
      Class<?> cls = Class.forName(NativeUtils.CLASS_NAME);
      Field f = null; //cls.getDeclaredField(NativeUtils.FIELD_NAME); //theUnsafe

      for (Field field : cls.getDeclaredFields()) {
        if (Modifier.isStatic(field.getModifiers()) && CLASS_NAME.equals(field.getType().getName())) {
          f = field;
          break;
        }
      }

      if (f != null) {
        f.setAccessible(true);
        u = (Unsafe) f.get(null);
      }
    } catch (Exception e) {
      UNSAFE = null;
      throw new RuntimeException(e);
    }

    if (u == null) {
      throw new RuntimeException("Unable to retrieve: " + CLASS_NAME);
    }

    UNSAFE = u;
  }

  /**
   * You <b>MUST KNOW</b> what are you doing.<br/>
   *
   * @return an 'unsafe' instance that contains many native methods <br/>
   * to work with JVM and memory directly passing most constraints and safety checks.
   */
  public static final Unsafe unsafe() {
    return NativeUtils.UNSAFE;
  }

  //####################################################################################################

  private NativeUtils() {
  }

  //####################################################################################################

  /**
   * Use this static class to security the heap memory directly.<br/>
   * <b>Comments and structure were copied from OpenJDK.</b>
   *
   * @author a.hofmann
   */
  public static final class DirectHeapAccess {

    /**
     * This constant differs from all results that will ever be returned from
     * {@link #staticFieldOffset}, {@link #objectFieldOffset},
     * or {@link #arrayBaseOffset}.
     */
    public static final int INVALID_FIELD_OFFSET = -1;

    /**
     * Allocate an instance but do not run any constructor.
     * Initializes the class if it has not yet been.
     *
     * @throws InstantiationException if there are some problems during instantiating
     */
    public static Object allocateInstance(Class<?> cls)
    throws InstantiationException {
      assert cls != null : "Class is null.";

      return UNSAFE.allocateInstance(cls);
    }

    /**
     * Fetches a value from a given Java variable.
     * More specifically, fetches a field or array element within the given
     * object <code>o</code> at the given offset, or (if <code>o</code> is
     * null) from the memory address whose numerical value is the given
     * offset.
     * <p>
     * The results are undefined unless one of the following cases is true:
     * <ul>
     * <li>The offset was obtained from {@link #objectFieldOffset} on
     * the {@link Field} of some Java field and the object
     * referred to by <code>o</code> is of a class compatible with that
     * field's class.
     * <p>
     * <li>The offset and object reference <code>o</code> (either null or
     * non-null) were both obtained via {@link #staticFieldOffset}
     * and {@link #staticFieldBase} (respectively) from the
     * reflective {@link Field} representation of some Java field.
     * <p>
     * <li>The object referred to by <code>o</code> is an array, and the offset
     * is an integer of the form <code>B+N*S</code>, where <code>N</code> is
     * a valid index into the array, and <code>B</code> and <code>S</code> are
     * the values obtained by {@link #arrayBaseOffset} and {@link
     * #arrayIndexScale} (respectively) from the array's class.  The value
     * referred to is the <code>N</code><em>th</em> element of the array.
     * <p>
     * </ul>
     * <p>
     * If one of the above cases is true, the call references a specific Java
     * variable (field or array element).  However, the results are undefined
     * if that variable is not in fact of the type returned by this method.
     * <p>
     * This method refers to a variable by means of two parameters, and so
     * it provides (in effect) a <em>double-register</em> addressing mode
     * for Java variables.  When the object reference is null, this method
     * uses its offset as an absolute address.  This is similar in operation
     * to methods such as {@link DirectMemoryAccess#getInt(long)}, which provide (in effect) a
     * <em>single-register</em> addressing mode for non-Java variables.
     * However, because Java variables may have a different layout in memory
     * from non-Java variables, programmers should not assume that these
     * two addressing modes are ever equivalent.  Also, programmers should
     * remember that offsets from the double-register addressing mode cannot
     * be portably confused with longs used in the single-register addressing
     * mode.
     *
     * @param o      Java heap object in which the variable resides, if any, else
     *               null
     * @param offset indication of where the variable resides in a Java heap
     *               object, if any, else a memory address locating the variable
     *               statically
     * @return the value fetched from the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *                          {@link NullPointerException}
     */
    public static int getInt(Object o,
                             long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getInt(o, offset);
    }

    /**
     * Stores a value into a given Java variable.
     * <p>
     * The first two parameters are interpreted exactly as with
     * {@link #getInt(Object, long)} to refer to a specific
     * Java variable (field or array element).  The given value
     * is stored into that variable.
     * <p>
     * The variable must be of the same type as the method
     * parameter <code>x</code>.
     *
     * @param o      Java heap object in which the variable resides, if any, else
     *               null
     * @param offset indication of where the variable resides in a Java heap
     *               object, if any, else a memory address locating the variable
     *               statically
     * @param x      the value to store into the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *                          {@link NullPointerException}
     */
    public static void putInt(Object o,
                              long offset,
                              int x) {
      assert o != null : "Object is null.";

      UNSAFE.putInt(o, offset, x);
    }

    /**
     * Fetches a reference value from a given Java variable.
     *
     * @see #getInt(Object, long)
     */
    public static Object getObject(Object o,
                                   long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getObject(o, offset);
    }

    /**
     * Stores a reference value into a given Java variable.
     * <p>
     * Unless the reference <code>x</code> being stored is either null
     * or matches the field type, the results are undefined.
     * If the reference <code>o</code> is non-null, car marks or
     * other store barriers for that object (if the VM requires them)
     * are updated.
     *
     * @see #putInt(Object, long, int)
     */
    public static void putObject(Object o,
                                 long offset,
                                 Object x) {
      assert o != null : "Object is null.";

      UNSAFE.putObject(o, offset, x);
    }

    /**
     * @see #getInt(Object, long)
     */
    public static boolean getBoolean(Object o,
                                     long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getBoolean(o, offset);
    }

    /**
     * @see #putInt(Object, long, int)
     */
    public static void putBoolean(Object o,
                                  long offset,
                                  boolean x) {
      assert o != null : "Object is null.";

      UNSAFE.putBoolean(o, offset, x);
    }

    /**
     * @see #getInt(Object, long)
     */
    public static byte getByte(Object o,
                               long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getByte(o, offset);
    }

    /**
     * @see #putInt(Object, long, int)
     */
    public static void putByte(Object o,
                               long offset,
                               byte x) {
      assert o != null : "Object is null.";

      UNSAFE.putByte(o, offset, x);
    }

    /**
     * @see #getInt(Object, long)
     */
    public static short getShort(Object o,
                                 long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getShort(o, offset);
    }

    /**
     * @see #putInt(Object, long, int)
     */
    public static void putShort(Object o,
                                long offset,
                                short x) {
      assert o != null : "Object is null.";

      UNSAFE.putShort(o, offset, x);
    }

    /**
     * @see #getInt(Object, long)
     */
    public static char getChar(Object o,
                               long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getChar(o, offset);
    }

    /**
     * @see #putInt(Object, long, int)
     */
    public static void putChar(Object o,
                               long offset,
                               char x) {
      assert o != null : "Object is null.";

      UNSAFE.putChar(o, offset, x);
    }

    /**
     * @see #getInt(Object, long)
     */
    public static long getLong(Object o,
                               long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getLong(o, offset);
    }

    /**
     * @see #putInt(Object, long, int)
     */
    public static void putLong(Object o,
                               long offset,
                               long x) {
      assert o != null : "Object is null.";

      UNSAFE.putLong(o, offset, x);
    }

    /**
     * @see #getInt(Object, long)
     */
    public static float getFloat(Object o,
                                 long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getFloat(o, offset);
    }

    /**
     * @see #putInt(Object, long, int)
     */
    public static void putFloat(Object o,
                                long offset,
                                float x) {
      assert o != null : "Object is null.";

      UNSAFE.putFloat(o, offset, x);
    }

    /**
     * @see #getInt(Object, long)
     */
    public static double getDouble(Object o,
                                   long offset) {
      assert o != null : "Object is null.";

      return UNSAFE.getDouble(o, offset);
    }

    /**
     * @see #putInt(Object, long, int)
     */
    public static void putDouble(Object o,
                                 long offset,
                                 double x) {
      assert o != null : "Object is null.";

      UNSAFE.putDouble(o, offset, x);
    }

    //####################################################################################################

    /**
     * Report the offset of the first element in the storage allocation of a
     * given array class.  If {@link #arrayIndexScale} returns a non-zero value
     * for the same class, you may use that scale factor, together with this
     * base offset, to form new offsets to security elements of arrays of the
     * given class.
     *
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public static int arrayBaseOffset(Class<?> arrayClass) {
      assert arrayClass != null : "Class is null.";

      return UNSAFE.arrayBaseOffset(arrayClass);
    }

    /**
     * Report the scale factor for addressing elements in the storage
     * allocation of a given array class.  However, arrays of "narrow" types
     * will generally not work properly with accessors like {@link
     * #getByte(Object, long)}, so the scale factor for such classes is reported
     * as zero.
     *
     * @see #arrayBaseOffset
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public static int arrayIndexScale(Class<?> arrayClass) {
      assert arrayClass != null : "Class is null.";

      return UNSAFE.arrayIndexScale(arrayClass);
    }

    /**
     * Report the location of a given static field, in conjunction with {@link
     * #staticFieldBase}.
     * <p>Do not expect to perform any sort of arithmetic on this offset;
     * it is just a cookie which is passed to the unsafe heap memory accessors.
     * <p>
     * <p>Any given field will always have the same offset, and no two distinct
     * fields of the same class will ever have the same offset.
     * <p>
     * <p>As of 1.4.1, offsets for fields are represented as long values,
     * although the Sun JVM does not use the most significant 32 bits.
     * It is hard to imagine a JVM technology which needs more than
     * a few bits to encode an offset within a non-array object,
     * However, for consistency with other methods in this class,
     * this method reports its result as a long value.
     *
     * @see #getInt(Object, long)
     */
    public static long objectFieldOffset(Field f) {
      assert f != null : "Field is null.";

      return UNSAFE.objectFieldOffset(f);
    }

    /**
     * Report the location of a given field in the storage allocation of its
     * class.  Do not expect to perform any sort of arithmetic on this offset;
     * it is just a cookie which is passed to the unsafe heap memory accessors.
     * <p>
     * <p>Any given field will always have the same offset and base, and no
     * two distinct fields of the same class will ever have the same offset
     * and base.
     * <p>
     * <p>As of 1.4.1, offsets for fields are represented as long values,
     * although the Sun JVM does not use the most significant 32 bits.
     * However, JVM implementations which store static fields at absolute
     * addresses can use long offsets and null base pointers to express
     * the field locations in a form usable by {@link #getInt(Object, long)}.
     * Therefore, code which will be ported to such JVMs on 64-bit platforms
     * must preserve all bits of static field offsets.
     *
     * @see #getInt(Object, long)
     */
    public static long staticFieldOffset(Field f) {
      assert f != null : "Field is null.";

      return UNSAFE.staticFieldOffset(f);
    }

    /**
     * Report the location of a given static field, in conjunction with {@link
     * #staticFieldOffset}.
     * <p>Fetch the base "Object", if any, with which static fields of the
     * given class can be accessed via methods like {@link #getInt(Object,
     * long)}.  This value may be null.  This value may refer to an object
     * which is a "cookie", not guaranteed to be a real Object, and it should
     * not be used in any way except as argument to the get and put routines in
     * this class.
     */
    public static Object staticFieldBase(Field f) {
      assert f != null : "Field is null.";

      return UNSAFE.staticFieldBase(f);
    }

    private DirectHeapAccess() {
    }
  }

  //####################################################################################################

  public static final class DirectClassLoadingAccess {

    /**
     * Ensure the given class has been initialized. This is often
     * needed in conjunction with obtaining the static field base of a
     * class.
     */
    public static void ensureClassInitialized(Class<?> cls) {
      assert cls != null : "Class is null.";

      UNSAFE.ensureClassInitialized(cls);
    }

    /// random trusted operations from JNI:

    /**
     * Tell the VM to define a class, without security checks. By default, the
     * class loader and protection domain come from the caller's class.
     *
     * @param name             of the class described in the byte code
     * @param byteCode         of the class
     * @param off              defines the beginning of byte code itself in the given array
     * @param len              defines the length of byte code in bytes
     * @param loader           to use for class loading
     * @param protectionDomain to use for security checks, but this is ignored in this case
     * @return a class instance with given parameters
     */
    public static Class<?> defineClass(String name,
                                       byte[] byteCode,
                                       int off,
                                       int len,
                                       ClassLoader loader,
                                       ProtectionDomain protectionDomain) {
      return UNSAFE.defineClass(name, byteCode, off, len, loader, protectionDomain);
    }

    /**
     * Define a class but do not make it known to the class loader or system dictionary.
     * <p>
     * For each CP (constant pool) entry, the corresponding CP patch must either be null or have
     * the a format that matches its tag:
     * <ul>
     * <li>Integer, Long, Float, Double: the corresponding wrapper object type from java.lang
     * <li>Utf8: a string (must have suitable syntax if used as signature or name)
     * <li>Class: any java.lang.Class object
     * <li>String: any object (not just a java.lang.String)
     * <li>InterfaceMethodRef: (NYI) a method handle to invoke on that call site's arguments
     * </ul>
     *
     * @param hostClass           context for linkage, access control, protection domain, and class loader
     * @param data                bytes of a class file
     * @param constantPoolPatches <b>non-null</b> entries replace corresponding constant-pool
     *                            entries in corresponding class. <br/>
     *                            In other words: not-null patch value under <code>index</code> replaces according CP
     *                            entry under the same <code>index</code>.
     * @return a class instance defined in the given byte-code
     */
    public static Class<?> defineAnonymousClass(Class<?> hostClass,
                                                byte[] data,
                                                Object... constantPoolPatches) {
      return UNSAFE.defineAnonymousClass(hostClass, data, constantPoolPatches);
    }
  }

  //####################################################################################################

  /**
   * Use this static class to security the direct-memory (aka. DirectByteBuffer).
   * This class forwards all method-calls to the underlying {@link Unsafe}-instance.
   * <b>Be careful, in some cases the JVM may crash without any exception etc.</b><br/>
   * <b>Comments and structure were copied from OpenJDK.</b>
   *
   * @author a.hofmann
   */
  public static final class DirectMemoryAccess {

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static byte getByte(long address) {
      return UNSAFE.getByte(address);
    }

    // These work on values in the C heap.

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #getByte(long)
     */
    public static void putByte(long address,
                               byte x) {
      UNSAFE.putByte(address, x);
    }

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static short getShort(long address) {
      return UNSAFE.getShort(address);
    }

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     */
    public static void putShort(long address,
                                short x) {
      UNSAFE.putShort(address, x);
    }

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static char getChar(long address) {
      return UNSAFE.getChar(address);
    }

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     */
    public static void putChar(long address,
                               char x) {
      UNSAFE.putChar(address, x);
    }

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static int getInt(long address) {
      return UNSAFE.getInt(address);
    }

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     */
    public static void putInt(long address,
                              int x) {
      UNSAFE.putInt(address, x);
    }

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static long getLong(long address) {
      return UNSAFE.getLong(address);
    }

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     */
    public static void putLong(long address,
                               long x) {
      UNSAFE.putLong(address, x);
    }

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static float getFloat(long address) {
      return UNSAFE.getFloat(address);
    }

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     */
    public static void putFloat(long address,
                                float x) {
      UNSAFE.putFloat(address, x);
    }

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static double getDouble(long address) {
      return UNSAFE.getDouble(address);
    }

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     */
    public static void putDouble(long address,
                                 double x) {
      UNSAFE.putDouble(address, x);
    }

    /**
     * Fetches a native pointer from a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     * <p>
     * <p> If the native pointer is less than 64 bits wide, it is extended as
     * an unsigned number to a Java long.  The pointer may be indexed by any
     * given byte offset, simply by adding that offset (as a simple integer) to
     * the long representing the pointer.  The number of bytes actually read
     * from the target address maybe determined by consulting {@link
     * #addressSize}.
     *
     * @see #allocateMemory
     */
    public static long getAddress(long address) {
      return UNSAFE.getAddress(address);
    }

    /**
     * Stores a native pointer into a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     * <p>
     * <p> The number of bytes actually written at the target address maybe
     * determined by consulting {@link #addressSize}.
     *
     * @see #getAddress(long)
     */
    public static void putAddress(long address,
                                  long x) {
      UNSAFE.putAddress(address, x);
    }

    /**
     * Allocates a new block of native memory, of the given size in bytes.  The
     * contents of the memory are uninitialized; they will generally be
     * garbage.  The resulting native pointer will never be zero, and will be
     * aligned for all value types.  Dispose of this memory by calling {@link
     * #freeMemory}, or resize it with {@link #reallocateMemory}.
     *
     * @throws IllegalArgumentException if the size is negative or too large
     *                                  for the native size_t type
     * @throws OutOfMemoryError         if the allocation is refused by the system
     * @see #getByte(long)
     * @see #putByte(long, byte)
     */
    public static long allocateMemory(long bytes) {
      return UNSAFE.allocateMemory(bytes);
    }

    /// wrappers for malloc, realloc, free:

    /**
     * Resizes a new block of native memory, to the given size in bytes.  The
     * contents of the new block past the size of the old block are
     * uninitialized; they will generally be garbage.  The resulting native
     * pointer will be zero if and only if the requested size is zero.  The
     * resulting native pointer will be aligned for all value types.  Dispose
     * of this memory by calling {@link #freeMemory}, or resize it with {@link
     * #reallocateMemory}.  The address passed to this method may be null, in
     * which case an allocation will be performed.
     *
     * @throws IllegalArgumentException if the size is negative or too large
     *                                  for the native size_t type
     * @throws OutOfMemoryError         if the allocation is refused by the system
     * @see #allocateMemory
     */
    public static long reallocateMemory(long address,
                                        long bytes) {
      return UNSAFE.reallocateMemory(address, bytes);
    }

    /**
     * Sets all bytes in a given block of memory to a fixed value
     * (usually zero).
     *
     * @param address of memory segment to fill
     * @param bytes   amount of bytes to set to the given value
     * @param value   to use
     */
    public static void setMemory(long address,
                                 long bytes,
                                 byte value) {
      UNSAFE.setMemory(address, bytes, value);
    }

    /**
     * Sets all bytes in a given block of memory to a copy of another
     * block.
     */
    public static void copyMemory(long srcAddress,
                                  long destAddress,
                                  long bytes) {
      UNSAFE.copyMemory(srcAddress, destAddress, bytes);
    }

    /**
     * Disposes of a block of native memory, as obtained from {@link
     * #allocateMemory} or {@link #reallocateMemory}.  The address passed to
     * this method may be null, in which case no action is taken.
     *
     * @see #allocateMemory
     */
    public static void freeMemory(long address) {
      UNSAFE.freeMemory(address);
    }

    /**
     * Report the size in bytes of a native pointer, as stored via {@link
     * #putAddress}.  This value will be either 4 or 8.  Note that the sizes of
     * other primitive types (as stored in native memory blocks) is determined
     * fully by their information content.
     */
    public static int addressSize() {
      return UNSAFE.addressSize();
    }

    //####################################################################################################

    /**
     * Report the size in bytes of a native memory page (whatever that is).
     * This value will always be a power of two.
     */
    public static int pageSize() {
      return UNSAFE.pageSize();
    }

    private DirectMemoryAccess() {
    }
  }

  //####################################################################################################

  /**
   * New opcode: LOCK XADD
   *
   * @since Java 1.8
   */
  public static final class DirectAtomicExchangerAccess {

    public static final int getAndAddInt(Object o,
                                         long offset,
                                         int value) {
      return UNSAFE.getAndAddInt(o, offset, value);
    }

    public static final long getAndAddLong(Object o,
                                           long offset,
                                           long value) {
      return UNSAFE.getAndAddLong(o, offset, value);
    }

    public static final int getAndSetInt(Object o,
                                         long offset,
                                         int value) {
      return UNSAFE.getAndSetInt(o, offset, value);
    }

    public static final long getAndSetLong(Object o,
                                           long offset,
                                           long value) {
      return UNSAFE.getAndSetLong(o, offset, value);
    }

    public static final Object getAndSetLong(Object o,
                                             long offset,
                                             Object value) {
      return UNSAFE.getAndSetObject(o, offset, value);
    }

    private DirectAtomicExchangerAccess() {
    }
  }

  /**
   * This static class represents all atomic/volatile operations, that are available in {@link Unsafe}-class.<br/>
   * <b>Comments and structure were copied from OpenJDK.</b>
   *
   * @author a.hofmann
   */
  public static final class DirectAtomicAccess {

    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     *
     * @return <tt>true</tt> if successful
     */
    public final static boolean compareAndSwapObject(Object o,
                                                     long offset,
                                                     Object expected,
                                                     Object x) {
      return UNSAFE.compareAndSwapObject(o, offset, expected, x);
    }

    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     *
     * @return <tt>true</tt> if successful
     */
    public final static boolean compareAndSwapInt(Object o,
                                                  long offset,
                                                  int expected,
                                                  int x) {
      return UNSAFE.compareAndSwapInt(o, offset, expected, x);
    }

    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     *
     * @return <tt>true</tt> if successful
     */
    public final static boolean compareAndSwapLong(Object o,
                                                   long offset,
                                                   long expected,
                                                   long x) {
      return UNSAFE.compareAndSwapLong(o, offset, expected, x);
    }

    /**
     * Fetches a reference value from a given Java variable, with volatile
     * load semantics. Otherwise identical to {@link DirectHeapAccess#getObject(Object, long)}
     */
    public static Object getObjectVolatile(Object o,
                                           long offset) {
      return UNSAFE.getObjectVolatile(o, offset);
    }

    /**
     * Stores a reference value into a given Java variable, with
     * volatile store semantics. Otherwise identical to {@link DirectHeapAccess#putObject(Object, long, Object)}
     */
    public static void putObjectVolatile(Object o,
                                         long offset,
                                         Object x) {
      UNSAFE.putObjectVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getInt(Object, long)}
     */
    public static int getIntVolatile(Object o,
                                     long offset) {
      return UNSAFE.getIntVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putInt(Object, long, int)}
     */
    public static void putIntVolatile(Object o,
                                      long offset,
                                      int x) {
      UNSAFE.putIntVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getBoolean(Object, long)}
     */
    public static boolean getBooleanVolatile(Object o,
                                             long offset) {
      return UNSAFE.getBooleanVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putBoolean(Object, long, boolean)}
     */
    public static void putBooleanVolatile(Object o,
                                          long offset,
                                          boolean x) {
      UNSAFE.putBooleanVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getByte(Object, long)}
     */
    public static byte getByteVolatile(Object o,
                                       long offset) {
      return UNSAFE.getByteVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putByte(Object, long, byte)}
     */
    public static void putByteVolatile(Object o,
                                       long offset,
                                       byte x) {
      UNSAFE.putByteVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getShort(Object, long)}
     */
    public static short getShortVolatile(Object o,
                                         long offset) {
      return UNSAFE.getShortVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putShort(Object, long, short)}
     */
    public static void putShortVolatile(Object o,
                                        long offset,
                                        short x) {
      UNSAFE.putShortVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getChar(Object, long)}
     */
    public static char getCharVolatile(Object o,
                                       long offset) {
      return UNSAFE.getCharVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putChar(Object, long, char)}
     */
    public static void putCharVolatile(Object o,
                                       long offset,
                                       char x) {
      UNSAFE.putCharVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getLong(Object, long)}
     */
    public static long getLongVolatile(Object o,
                                       long offset) {
      return UNSAFE.getLongVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putLong(Object, long, long)}
     */
    public static void putLongVolatile(Object o,
                                       long offset,
                                       long x) {
      UNSAFE.putLongVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getFloat(Object, long)}
     */
    public static float getFloatVolatile(Object o,
                                         long offset) {
      return UNSAFE.getFloatVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putFloat(Object, long, float)}
     */
    public static void putFloatVolatile(Object o,
                                        long offset,
                                        float x) {
      UNSAFE.putFloatVolatile(o, offset, x);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#getDouble(Object, long)}
     */
    public static double getDoubleVolatile(Object o,
                                           long offset) {
      return UNSAFE.getDoubleVolatile(o, offset);
    }

    /**
     * Volatile version of {@link DirectHeapAccess#putDouble(Object, long, double)}
     */
    public static void putDoubleVolatile(Object o,
                                         long offset,
                                         double x) {
      UNSAFE.putDouble(o, offset, x);
    }

    /**
     * Version of {@link #putObjectVolatile(Object, long, Object)}<br/>
     * that does not guarantee immediate visibility of the store to
     * other threads.<br/>This method is generally only useful if the
     * underlying field is a Java volatile <br/>(or if an array cell,
     * one that is otherwise only accessed using volatile accesses).
     */
    public static void putOrderedObject(Object o,
                                        long offset,
                                        Object x) {
      UNSAFE.putOrderedObject(o, offset, x);
    }

    /**
     * Ordered/Lazy version of {@link #putIntVolatile(Object, long, int)}
     */
    public static void putOrderedInt(Object o,
                                     long offset,
                                     int x) {
      UNSAFE.putOrderedInt(o, offset, x);
    }

    /**
     * Ordered/Lazy version of {@link #putLongVolatile(Object, long, long)}
     */
    public static void putOrderedLong(Object o,
                                      long offset,
                                      long x) {
      UNSAFE.putOrderedLong(o, offset, x);
    }

    private DirectAtomicAccess() {
    }
  }

  //####################################################################################################

  /**
   * Allows to influence memory access reordering for different access kinds (load/store)
   *
   * @since Java 1.8
   */
  public static final class DirectFenceControl {

    /**
     * Ensures lack of reordering of loads before the fence
     * with loads or stores after the fence.
     */
    public static void loadFence() {
      UNSAFE.loadFence();
    }

    /**
     * Ensures lack of reordering of stores before the fence
     * with loads or stores after the fence.
     */
    public static void storeFence() {
      UNSAFE.storeFence();
    }

    /**
     * Ensures lack of reordering of loads or stores before the fence
     * with loads or stores after the fence.
     */
    public static void fullFence() {
      UNSAFE.fullFence();
    }

    private DirectFenceControl() {
    }
  }

  //####################################################################################################

  /**
   * This class represents all methods to control execution flow of a thread.<br/>
   * <b>Comments and structure were copied from OpenJDK.</b>
   *
   * @author a.hofmann
   */
  public static final class DirectThreadControl {

    /**
     * Lock the object.  It must get unlocked via {@link #monitorExit}.<br/>Analog to <code>synchronized(o){ ....
     * }</code>
     */
    public static void monitorEnter(Object o) {
      UNSAFE.monitorEnter(o);
    }

    /**
     * Unlock the object.  It must have been locked via {@link #monitorEnter}.
     */
    public static void monitorExit(Object o) {
      UNSAFE.monitorExit(o);
    }

    /**
     * Tries to lock the object.  Returns true or false to indicate
     * whether the lock succeeded.  If it did, the object must be
     * unlocked via {@link #monitorExit}.
     */
    public static boolean tryMonitorEnter(Object o) {
      return UNSAFE.tryMonitorEnter(o);
    }

    /**
     * Unblock the given thread blocked on <tt>park</tt>, or, if it is
     * not blocked, cause the subsequent call to <tt>park</tt> not to
     * block.  Note: this operation is "unsafe" solely because the
     * caller must somehow ensure that the thread has not been
     * destroyed. Nothing special is usually required to ensure this
     * when called from Java (in which there will ordinarily be a live
     * reference to the thread) but this is not nearly-automatically
     * so when calling from native code.
     *
     * @param thread the thread to unpark.
     */
    public static void unpark(Object thread) {
      UNSAFE.unpark(thread);
    }

    /**
     * Block current thread, returning when a balancing
     * <b>unpark</b> occurs, or a balancing <b>unpark</b> has
     * already occurred, or the thread is interrupted, or, if not
     * absolute and time is not zero, the given time nanoseconds have
     * elapsed, or if absolute, the given deadline in milliseconds
     * since Epoch has passed, or spuriously (i.e., returning for no
     * "reason"). Note: This operation is in the Unsafe class only
     * because <b>unpark</b> is, so it would be strange to place it
     * elsewhere.
     */
    public static void park(boolean isAbsolute,
                            long time) {
      UNSAFE.park(isAbsolute, time);
    }

    /**
     * Throws the exception without telling the verifier.
     * So you can basically throw any checked exception without to mark the throwing method with
     * <b>throws</b>-keyword.
     *
     * @param e is the exception you want to be thrown.
     */
    public static void throwException(Throwable e) {
      UNSAFE.throwException(e);
    }

    private DirectThreadControl() {
    }
  }

  //####################################################################################################

  /**
   * This class uses the native/direct security on the memory of a given array via JNI service-class {@link Unsafe}.
   * This may be in some cases very dangerous because the whole <b>JVM may crash</b> without even to say GOOD BYE
   * (any errors/exceptions)!
   *
   * @author a.hofmann
   */
  public static final class DirectArrayAccess {

    final static long BOOLEAN_ARRAY_OFFSET;
    final static long BYTE_ARRAY_OFFSET;
    final static long CHAR_ARRAY_OFFSET;
    final static long SHORT_ARRAY_OFFSET;
    final static long INT_ARRAY_OFFSET;
    final static long FLOAT_ARRAY_OFFSET;
    final static long LONG_ARRAY_OFFSET;
    final static long DOUBLE_ARRAY_OFFSET;
    final static long REF_ARRAY_OFFSET;

    final static int BOOLEAN_INDEX_SCALE;
    final static int BYTE_INDEX_SCALE;
    final static int CHAR_INDEX_SCALE;
    final static int SHORT_INDEX_SCALE;
    final static int INT_INDEX_SCALE;
    final static int FLOAT_INDEX_SCALE;
    final static int LONG_INDEX_SCALE;
    final static int DOUBLE_INDEX_SCALE;
    final static int REF_INDEX_SCALE;

    static {
      int booleanArrayOffset = 0;
      int booleanIndexScale = 0;
      int byteArrayOffset = 0;
      int byteIndexScale = 0;
      int charArrayOffset = 0;
      int charIndexScale = 0;
      int shortArrayOffset = 0;
      int shortIndexScale = 0;
      int intArrayOffset = 0;
      int intIndexScale = 0;
      int floatArrayOffset = 0;
      int floatIndexScale = 0;
      int longArrayOffset = 0;
      int longIndexScale = 0;
      int doubleArrayOffset = 0;
      int doubleIndexScale = 0;

      int refArrayOffset = 0;
      int refIndexScale = 0;


      try {
        booleanArrayOffset = UNSAFE.arrayBaseOffset(boolean[].class);
        booleanIndexScale = UNSAFE.arrayIndexScale(boolean[].class);

        byteArrayOffset = UNSAFE.arrayBaseOffset(byte[].class);
        byteIndexScale = UNSAFE.arrayIndexScale(byte[].class);

        charArrayOffset = UNSAFE.arrayBaseOffset(char[].class);
        charIndexScale = UNSAFE.arrayIndexScale(char[].class);

        shortArrayOffset = UNSAFE.arrayBaseOffset(short[].class);
        shortIndexScale = UNSAFE.arrayIndexScale(short[].class);

        intArrayOffset = UNSAFE.arrayBaseOffset(int[].class);
        intIndexScale = UNSAFE.arrayIndexScale(int[].class);

        floatArrayOffset = UNSAFE.arrayBaseOffset(float[].class);
        floatIndexScale = UNSAFE.arrayIndexScale(float[].class);

        longArrayOffset = UNSAFE.arrayBaseOffset(long[].class);
        longIndexScale = UNSAFE.arrayIndexScale(long[].class);

        doubleArrayOffset = UNSAFE.arrayBaseOffset(double[].class);
        doubleIndexScale = UNSAFE.arrayIndexScale(double[].class);

        refArrayOffset = UNSAFE.arrayBaseOffset(Object[].class);
        refIndexScale = UNSAFE.arrayIndexScale(Object[].class);
      } catch (Throwable t) {
        throw new RuntimeException(t);
      } finally {
        BOOLEAN_ARRAY_OFFSET = booleanArrayOffset;
        BOOLEAN_INDEX_SCALE = calculateElementScaleShift(booleanIndexScale);

        BYTE_ARRAY_OFFSET = byteArrayOffset;
        BYTE_INDEX_SCALE = calculateElementScaleShift(byteIndexScale);

        CHAR_ARRAY_OFFSET = charArrayOffset;
        CHAR_INDEX_SCALE = calculateElementScaleShift(charIndexScale);

        SHORT_ARRAY_OFFSET = shortArrayOffset;
        SHORT_INDEX_SCALE = calculateElementScaleShift(shortIndexScale);

        INT_ARRAY_OFFSET = intArrayOffset;
        INT_INDEX_SCALE = calculateElementScaleShift(intIndexScale);

        FLOAT_ARRAY_OFFSET = floatArrayOffset;
        FLOAT_INDEX_SCALE = calculateElementScaleShift(floatIndexScale);

        LONG_ARRAY_OFFSET = longArrayOffset;
        LONG_INDEX_SCALE = calculateElementScaleShift(longIndexScale);

        DOUBLE_ARRAY_OFFSET = doubleArrayOffset;
        DOUBLE_INDEX_SCALE = calculateElementScaleShift(doubleIndexScale);

        REF_ARRAY_OFFSET = refArrayOffset;
        REF_INDEX_SCALE = calculateElementScaleShift(refIndexScale);
      }
    }

    private static int calculateElementScaleShift(int scale) {
      switch (scale) {
        case 1:
          return 0;
        case 2:
          return 1;
        case 4:
          return 2;
        case 8:
          return 3;
      }
      throw new IllegalStateException("Unsupported scale factor: " + scale);
    }

    public static long booleanIndex(long idx) {
      return BOOLEAN_ARRAY_OFFSET + (idx << BOOLEAN_INDEX_SCALE);
    }

    //#####################################################################################################

    public static boolean read(boolean[] array,
                               int idx) {
      return UNSAFE.getBoolean(array, booleanIndex(idx));
    }

    public static boolean readVolatile(boolean[] array,
                                       int idx) {
      return UNSAFE.getBooleanVolatile(array, booleanIndex(idx));
    }

    public static void write(boolean[] array,
                             int idx,
                             boolean value) {
      UNSAFE.putBoolean(array, booleanIndex(idx), value);
    }

    public static void writeVolatile(boolean[] array,
                                     int idx,
                                     boolean value) {
      UNSAFE.putBooleanVolatile(array, booleanIndex(idx), value);
    }

    public static long byteIndex(long idx) {
      return BYTE_ARRAY_OFFSET + (idx << BYTE_INDEX_SCALE);
    }

    //#####################################################################################################

    public static byte read(byte[] array,
                            int idx) {
      return UNSAFE.getByte(array, byteIndex(idx));
    }

    public static byte readVolatile(byte[] array,
                                    int idx) {
      return UNSAFE.getByteVolatile(array, byteIndex(idx));
    }

    public static void write(byte[] array,
                             int idx,
                             byte value) {
      UNSAFE.putByte(array, byteIndex(idx), value);
    }

    public static void writeVolatile(byte[] array,
                                     int idx,
                                     byte value) {
      UNSAFE.putByteVolatile(array, byteIndex(idx), value);
    }

    public static long charIndex(long idx) {
      return CHAR_ARRAY_OFFSET + (idx << CHAR_INDEX_SCALE);
    }

    //#####################################################################################################

    public static char read(char[] array,
                            int idx) {
      return UNSAFE.getChar(array, charIndex(idx));
    }

    public static char readVolatile(char[] array,
                                    int idx) {
      return UNSAFE.getCharVolatile(array, charIndex(idx));
    }

    public static void write(char[] array,
                             int idx,
                             char value) {
      UNSAFE.putChar(array, charIndex(idx), value);
    }

    public static void writeVolatile(char[] array,
                                     int idx,
                                     char value) {
      UNSAFE.putCharVolatile(array, charIndex(idx), value);
    }

    public static long shortIndex(long idx) {
      return SHORT_ARRAY_OFFSET + (idx << SHORT_INDEX_SCALE);
    }

    //#####################################################################################################

    public static short read(short[] array,
                             int idx) {
      return UNSAFE.getShort(array, shortIndex(idx));
    }

    public static short readVolatile(short[] array,
                                     int idx) {
      return UNSAFE.getShortVolatile(array, shortIndex(idx));
    }

    public static void write(short[] array,
                             int idx,
                             short value) {
      UNSAFE.putShort(array, shortIndex(idx), value);
    }

    public static void writeVolatile(short[] array,
                                     int idx,
                                     short value) {
      UNSAFE.putShortVolatile(array, shortIndex(idx), value);
    }

    public static long intIndex(long idx) {
      return INT_ARRAY_OFFSET + (idx << INT_INDEX_SCALE);
    }

    //#####################################################################################################

    public static int read(int[] array,
                           int idx) {
      return UNSAFE.getInt(array, intIndex(idx));
    }

    public static int readVolatile(int[] array,
                                   int idx) {
      return UNSAFE.getIntVolatile(array, intIndex(idx));
    }

    public static void write(int[] array,
                             int idx,
                             int value) {
      UNSAFE.putInt(array, intIndex(idx), value);
    }

    public static void writeVolatile(int[] array,
                                     int idx,
                                     int value) {
      UNSAFE.putIntVolatile(array, intIndex(idx), value);
    }

    public static long floatIndex(long idx) {
      return FLOAT_ARRAY_OFFSET + (idx << FLOAT_INDEX_SCALE);
    }

    //#####################################################################################################

    public static float read(float[] array,
                             int idx) {
      return UNSAFE.getFloat(array, floatIndex(idx));
    }

    public static float readVolatile(float[] array,
                                     int idx) {
      return UNSAFE.getFloatVolatile(array, floatIndex(idx));
    }

    public static void write(float[] array,
                             int idx,
                             float value) {
      UNSAFE.putFloat(array, floatIndex(idx), value);
    }

    public static void writeVolatile(float[] array,
                                     int idx,
                                     float value) {
      UNSAFE.putFloatVolatile(array, floatIndex(idx), value);
    }

    public static long longIndex(long idx) {
      return LONG_ARRAY_OFFSET + (idx << LONG_INDEX_SCALE);
    }

    //#####################################################################################################

    public static long read(long[] array,
                            int idx) {
      return UNSAFE.getLong(array, longIndex(idx));
    }

    public static long readVolatile(long[] array,
                                    int idx) {
      return UNSAFE.getLongVolatile(array, longIndex(idx));
    }

    public static void write(long[] array,
                             int idx,
                             long value) {
      UNSAFE.putLong(array, longIndex(idx), value);
    }

    public static void writeVolatile(long[] array,
                                     int idx,
                                     long value) {
      UNSAFE.putLongVolatile(array, longIndex(idx), value);
    }

    public static long doubleIndex(long idx) {
      return DOUBLE_ARRAY_OFFSET + (idx << DOUBLE_INDEX_SCALE);
    }

    //#####################################################################################################

    public static double read(double[] array,
                              int idx) {
      return UNSAFE.getDouble(array, doubleIndex(idx));
    }

    public static double readVolatile(double[] array,
                                      int idx) {
      return UNSAFE.getDoubleVolatile(array, doubleIndex(idx));
    }

    public static void write(double[] array,
                             int idx,
                             double value) {
      UNSAFE.putDouble(array, doubleIndex(idx), value);
    }

    public static void writeVolatile(double[] array,
                                     int idx,
                                     double value) {
      UNSAFE.putDoubleVolatile(array, doubleIndex(idx), value);
    }

    /**
     * @param idx of the wanted element
     * @return aligned index of wanted element
     */
    public static long refIndex(long idx) {
      return REF_ARRAY_OFFSET + (idx << REF_INDEX_SCALE);
    }

    //#####################################################################################################

    public static Object read(Object[] array,
                              int idx) {
      return UNSAFE.getObject(array, refIndex(idx));
    }

    public static Object readVolatile(Object[] array,
                                      int idx) {
      return UNSAFE.getObjectVolatile(array, refIndex(idx));
    }

    public static void write(Object[] array,
                             int idx,
                             Object value) {
      UNSAFE.putObject(array, refIndex(idx), value);
    }

    public static void writeVolatile(Object[] array,
                                     int idx,
                                     Object value) {
      UNSAFE.putObjectVolatile(array, refIndex(idx), value);
    }

    private DirectArrayAccess() {
    }

    //#####################################################################################################
  }
}
