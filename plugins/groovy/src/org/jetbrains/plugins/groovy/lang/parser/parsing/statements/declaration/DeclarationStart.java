package org.jetbrains.plugins.groovy.lang.parser.parsing.statements.declaration;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.groovy.GroovyBundle;
import org.jetbrains.plugins.groovy.lang.parser.parsing.Construction;
import org.jetbrains.plugins.groovy.lang.parser.parsing.auxiliary.Modifier;
import org.jetbrains.plugins.groovy.lang.parser.parsing.identifier.UpperCaseIdent;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.auxilary.BalancedTokens;
import org.jetbrains.plugins.groovy.lang.parser.parsing.types.BuiltlnType;
import org.jetbrains.plugins.groovy.lang.parser.parsing.types.QualifiedTypeName;
import org.jetbrains.plugins.groovy.lang.parser.parsing.util.ParserUtils;

/**
 * @autor: Dmitry.Krasilschikov
 * @date: 14.03.2007
 */

/**
 * DeclarationStart ::= "def"
 * | modifier
 * | @IDENT
 * | (upperCaseIdent | builtlnType | QulifiedTypeName)  {LBRACK balacedTokens RBRACK} IDENT
 */

public class DeclarationStart implements Construction {
  public static IElementType parse(PsiBuilder builder) {
    IElementType elementType;

    //def
    if (ParserUtils.getToken(builder, kDEF)) return kDEF;

    //Modifier
    elementType = Modifier.parse(builder);
    if (!tWRONG_SET.contains(elementType)) return elementType;

    //@IDENT

    if (ParserUtils.getToken(builder, mAT)) {
      if (!ParserUtils.getToken(builder, mIDENT, GroovyBundle.message("identifier.expected"))) {
        return WRONGWAY;
      }

      return DECLARATION_START;
    }

    // (upperCaseIdent | builtlnType | QulifiedTypeName)  {LBRACK balacedTokens RBRACK} IDENT
    if (!tWRONG_SET.contains(UpperCaseIdent.parse(builder)) || !tWRONG_SET.contains(BuiltlnType.parse(builder)) || !tWRONG_SET.contains(QualifiedTypeName.parse(builder))) {

      IElementType balancedTokens;

      do {
        balancedTokens = parseBalancedTokensInBrackets(builder);

        if (!BALANCED_TOKENS.equals(balancedTokens)) {
          return WRONGWAY;
        }
      } while (!tWRONG_SET.contains(balancedTokens));

      //IDENT
      if (!ParserUtils.getToken(builder, mIDENT, GroovyBundle.message("identifier.expected"))) {
        return WRONGWAY;
      } else {
        return DECLARATION_START;
      }

    } else {
      builder.error(GroovyBundle.message("upper.case.ident.or.builtln.type.or.qualified.type.name.expected"));
      return WRONGWAY;
    }
  }

  private static IElementType parseBalancedTokensInBrackets(PsiBuilder builder) {
    PsiBuilder.Marker btm = builder.mark();


    if (!ParserUtils.getToken(builder, mLBRACK, GroovyBundle.message("lbrack.expected"))) {
      btm.drop();
      return WRONGWAY;
    }

    if (tWRONG_SET.contains(BalancedTokens.parse(builder))) {
      btm.rollbackTo();
      return WRONGWAY;
    }

    if (!ParserUtils.getToken(builder, mRBRACK, GroovyBundle.message("rbrack.expected"))) {
      btm.rollbackTo();
      return WRONGWAY;
    }

    btm.done(BALANCED_TOKENS);

    return BALANCED_TOKENS;
  }
}

