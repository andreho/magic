package net.andreho.magic;

import sun.reflect.Magic;

/**
 * All sub-classes don't have any kind of byte-code verification -
 * <u>use with care</u> because any malformed byte-code won't be reported back and 
 * would possibly crash the whole JVM.
 * <br/>Created by a.hofmann on 21.04.2016.
 *
 * @see sun.reflect.Magic
 */
public class MagicAccess extends Magic {
}
