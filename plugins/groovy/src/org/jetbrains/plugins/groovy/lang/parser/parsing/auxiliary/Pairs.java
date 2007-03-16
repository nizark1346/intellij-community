package org.jetbrains.plugins.groovy.lang.parser.parsing.auxiliary;

import com.intellij.psi.tree.IElementType;
import static org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @autor: Dmitry.Krasilschikov
 * @date: 16.03.2007
 */
public abstract class Pairs {
  public static Map<IElementType, IElementType> pairElementsMap = new HashMap<IElementType, IElementType>();
  static {
    pairElementsMap.put(mLPAREN, mRPAREN);
    pairElementsMap.put(mRPAREN, mLPAREN);

    pairElementsMap.put(mLBRACK, mRBRACK);
    pairElementsMap.put(mRBRACK, mLBRACK);

    pairElementsMap.put(mLCURLY, mRCURLY);
    pairElementsMap.put(mRCURLY, mLCURLY);

    pairElementsMap.put(mSTRING_CTOR_START, mSTRING_CTOR_END);
    pairElementsMap.put(mSTRING_CTOR_END, mSTRING_CTOR_START);
  }
}
