/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.expression

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExpressionMatchTest {
  @Test
  fun testEmptyPattern() {
    // Empty pattern
    val emptyMatch = ExpressionMatch("", ExpressionMatch.MatchMode.MatchPhrase, false)
    assertNull(emptyMatch.positiveRegex)
    assertNull(emptyMatch.negativeRegex)
    // Assert empty is valid
    // Assert empty
    assertTrue(emptyMatch.isEmpty())
    // Assert default match fails (same as setting match empty to false)
    assertFalse(emptyMatch.match("something"))
    // Assert match empty succeeds
    assertTrue(emptyMatch.match("something", true))
  }

  @Test
  fun testMatchPhrase() {
    // Simple phrase, case-insensitive
    val simpleMatch = ExpressionMatch("test", ExpressionMatch.MatchMode.MatchPhrase, false)
    // Simple phrase, case-sensitive
    val simpleMatchCS = ExpressionMatch("test", ExpressionMatch.MatchMode.MatchPhrase, true)
    // Phrase with space, case-insensitive
    val simpleMatchSpace = ExpressionMatch(" space ", ExpressionMatch.MatchMode.MatchPhrase, true)
    // Complex phrase
    val complexMatchFull = """^(?:norm|norm\-space|\!norm\-escaped|\\\!slash\-invert|\\\\double|escape\;""" +
      """sep|slash\-end\-split\\|quad\\\\\!noninvert|newline\-split|newline\-split\-slash\\|slash\-at\-end\\)$"""
    val complexMatch = ExpressionMatch(
      complexMatchFull,
      ExpressionMatch.MatchMode.MatchPhrase,
      false
    )

    assertEquals("(?:^|\\W)test(?:\\W|$)", simpleMatch.positiveRegex?.pattern)
    assertEquals(setOf(RegexOption.IGNORE_CASE), simpleMatch.positiveRegex?.options)
    assertNull(simpleMatch.negativeRegex)

    assertEquals("(?:^|\\W)test(?:\\W|$)", simpleMatchCS.positiveRegex?.pattern)
    assertEquals(emptySet<RegexOption>(), simpleMatchCS.positiveRegex?.options)
    assertNull(simpleMatchCS.negativeRegex)

    assertEquals("(?:^|\\W) space (?:\\W|$)", simpleMatchSpace.positiveRegex?.pattern)
    assertEquals(emptySet<RegexOption>(), simpleMatchSpace.positiveRegex?.options)
    assertNull(simpleMatchSpace.negativeRegex)

    // Assert valid and not empty
    assertFalse(simpleMatch.isEmpty())
    assertFalse(simpleMatchCS.isEmpty())
    assertFalse(simpleMatchSpace.isEmpty())
    assertFalse(complexMatch.isEmpty())

    // Assert match succeeds
    assertTrue(simpleMatch.match("test"))
    assertTrue(simpleMatch.match("other test;"))
    assertTrue(simpleMatchSpace.match(" space "))
    // Assert partial match fails
    assertFalse(simpleMatch.match("testing"))
    assertFalse(simpleMatchSpace.match("space"))
    // Assert unrelated fails
    assertFalse(simpleMatch.match("not above"))

    // Assert case sensitivity followed
    assertFalse(simpleMatch.caseSensitive)
    assertTrue(simpleMatch.match("TeSt"))
    assertTrue(simpleMatchCS.caseSensitive)
    assertFalse(simpleMatchCS.match("TeSt"))

    // Assert complex phrases are escaped properly
    assertTrue(complexMatch.match(complexMatchFull))
    assertFalse(complexMatch.match("norm"))
  }

  @Test
  fun matchMultiPhrase() {
    // Simple phrases, case-insensitive
    val simpleMatch = ExpressionMatch(
      "test\nOther ",
      ExpressionMatch.MatchMode.MatchMultiPhrase,
      false
    )
    // Simple phrases, case-sensitive
    val simpleMatchCS = ExpressionMatch(
      "test\nOther ",
      ExpressionMatch.MatchMode.MatchMultiPhrase,
      true
    )
    // Complex phrases
    val complexMatchFullA =
      """^(?:norm|norm\-space|\!norm\-escaped|\\\!slash\-invert|\\\\double)|escape\;""" +
        """sep|slash\-end\-split\\|quad\\\\\!noninvert)|newline\-split|newline\-split\-slash\\|slash\-at\-end\\)$"""
    val complexMatchFullB = """^(?:invert|invert\-space)$)$"""
    val complexMatch = ExpressionMatch(
      complexMatchFullA + "\n" + complexMatchFullB,
      ExpressionMatch.MatchMode.MatchMultiPhrase,
      false
    )

    // Assert valid and not empty
    assertFalse(simpleMatch.isEmpty())
    assertFalse(simpleMatchCS.isEmpty())
    assertFalse(complexMatch.isEmpty())

    // Assert match succeeds
    assertTrue(simpleMatch.match("test"))
    assertTrue(simpleMatch.match("test[suffix]"))
    assertTrue(simpleMatch.match("other test;"))
    assertTrue(simpleMatch.match("Other "))
    assertTrue(simpleMatch.match(".Other !"))
    // Assert partial match fails
    assertFalse(simpleMatch.match("testing"))
    assertFalse(simpleMatch.match("Other!"))
    // Assert unrelated fails
    assertFalse(simpleMatch.match("not above"))

    // Assert case sensitivity followed
    assertFalse(simpleMatch.caseSensitive)
    assertTrue(simpleMatch.match("TeSt"))
    assertTrue(simpleMatchCS.caseSensitive)
    assertFalse(simpleMatchCS.match("TeSt"))

    // Assert complex phrases are escaped properly
    assertTrue(complexMatch.match(complexMatchFullA))
    assertTrue(complexMatch.match(complexMatchFullB))
    assertFalse(complexMatch.match("norm"))
    assertFalse(complexMatch.match("invert"))
  }

  @Test
  fun matchWildcard() {
    // Simple wildcard, case-insensitive
    val simpleMatch =
      ExpressionMatch("?test*", ExpressionMatch.MatchMode.MatchWildcard, false)
    // Simple wildcard, case-sensitive
    val simpleMatchCS =
      ExpressionMatch("?test*", ExpressionMatch.MatchMode.MatchWildcard, true)
    // Escaped wildcard, case-insensitive
    val simpleMatchEscape =
      ExpressionMatch("""\?test\*""", ExpressionMatch.MatchMode.MatchWildcard, false)
    // Inverted wildcard, case-insensitive
    val simpleMatchInvert =
      ExpressionMatch("!test*", ExpressionMatch.MatchMode.MatchWildcard, false)
    // Not inverted wildcard, case-insensitive
    val simpleMatchNoInvert =
      ExpressionMatch("""\!test*""", ExpressionMatch.MatchMode.MatchWildcard, false)
    // Not inverted wildcard literal slash, case-insensitive
    val simpleMatchNoInvertSlash =
      ExpressionMatch("""\\!test*""", ExpressionMatch.MatchMode.MatchWildcard, false)
    // Complex wildcard
    val complexMatch =
      ExpressionMatch(
        """never?gonna*give\*you\?up\\test|y\yeah\\1\\\\2\\\1inval""",
        ExpressionMatch.MatchMode.MatchWildcard, false
      )

    // Assert valid and not empty
    assertFalse(simpleMatch.isEmpty())
    assertFalse(simpleMatchCS.isEmpty())
    assertFalse(simpleMatchEscape.isEmpty())
    assertFalse(simpleMatchInvert.isEmpty())
    assertFalse(simpleMatchNoInvert.isEmpty())
    assertFalse(simpleMatchNoInvertSlash.isEmpty())
    assertFalse(complexMatch.isEmpty())

    // Assert match succeeds
    assertTrue(simpleMatch.match("@test"))
    assertTrue(simpleMatch.match("@testing"))
    assertTrue(simpleMatch.match("!test"))
    assertTrue(simpleMatchEscape.match("?test*"))
    assertTrue(simpleMatchInvert.match("atest"))
    assertTrue(simpleMatchNoInvert.match("!test"))
    assertTrue(simpleMatchNoInvertSlash.match("""\!test)"""))
    // Assert partial match fails
    assertFalse(simpleMatch.match("test"))
    // Assert unrelated fails
    assertFalse(simpleMatch.match("not above"))
    // Assert escaped wildcard fails
    assertFalse(simpleMatchEscape.match("@testing"))
    assertFalse(simpleMatchNoInvert.match("test"))
    assertFalse(simpleMatchNoInvert.match("anything"))
    assertFalse(simpleMatchNoInvertSlash.match("!test"))
    assertFalse(simpleMatchNoInvertSlash.match("test"))
    assertFalse(simpleMatchNoInvertSlash.match("anything"))
    // Assert non-inverted fails
    assertFalse(simpleMatchInvert.match("testing"))

    // Assert case sensitivity followed
    assertFalse(simpleMatch.caseSensitive)
    assertTrue(simpleMatch.match("@TeSt"))
    assertTrue(simpleMatchCS.caseSensitive)
    assertFalse(simpleMatchCS.match("@TeSt"))

    // Assert complex match
    assertTrue(complexMatch.match("""neverAgonnaBBBgive*you?up\test|yyeah\1\\2\1inval"""))
    // Assert complex not literal match
    assertFalse(complexMatch.match("""never?gonna*give\*you\?up\\test|y\yeah\\1\\\\2\\\1inval"""))
    // Assert complex unrelated not match
    assertFalse(complexMatch.match("other"))
  }

  @Test
  fun matchMultiWildcard() {
    val emptyMatch =
      ExpressionMatch(
        ";!\n;",
        ExpressionMatch.MatchMode.MatchMultiWildcard, false
      )
    val simpleMatch =
      ExpressionMatch(
        "?test*;another?",
        ExpressionMatch.MatchMode.MatchMultiWildcard, false
      )
    val simpleMatchCS =
      ExpressionMatch(
        "?test*;another?",
        ExpressionMatch.MatchMode.MatchMultiWildcard, true
      )
    val simpleMatchEscape =
      ExpressionMatch(
        "\\?test\\*\\\n*thing\\*",
        ExpressionMatch.MatchMode.MatchMultiWildcard, false
      )
    val simpleMatchInvert =
      ExpressionMatch(
        """test*;!testing;\!testing""",
        ExpressionMatch.MatchMode.MatchMultiWildcard, false
      )
    val simpleMatchImplicit =
      ExpressionMatch(
        """!testing*""",
        ExpressionMatch.MatchMode.MatchMultiWildcard, false
      )
    val complexMatchFull = """norm;!invert; norm-space ; !invert-space ;;!;\!norm-escaped;""" +
      """\\!slash-invert;\\\\double; escape\;sep;slash-end-split\\;""" +
      """quad\\\\!noninvert;newline-split""" + "\n" +
      """newline-split-slash\\""" + "\n" +
      """slash-at-end\\"""

    // Match normal components
    val complexMatchNormal = listOf(
      """norm""",
      """norm-space""",
      """!norm-escaped""",
      """\!slash-invert""",
      """\\double""",
      """escape;sep""",
      """slash-end-split\""",
      """quad\\!noninvert""",
      """newline-split""",
      """newline-split-slash\""",
      """slash-at-end\"""
    )
    // Match negating components
    val complexMatchInvert = listOf(
      """invert""",
      """invert-space"""
    )
    val complexMatch = ExpressionMatch(
      complexMatchFull, ExpressionMatch.MatchMode.MatchMultiWildcard,
      false
    )

    // Assert valid and not empty
    assertTrue(emptyMatch.isEmpty())
    assertFalse(simpleMatch.isEmpty())
    assertFalse(simpleMatchCS.isEmpty())
    assertFalse(simpleMatchEscape.isEmpty())
    assertFalse(simpleMatchInvert.isEmpty())
    assertFalse(simpleMatchImplicit.isEmpty())
    assertFalse(complexMatch.isEmpty())

    // Assert match succeeds
    assertTrue(simpleMatch.match("@test"))
    assertTrue(simpleMatch.match("@testing"))
    assertTrue(simpleMatch.match("!test"))
    assertTrue(simpleMatch.match("anotherA"))
    assertTrue(simpleMatchEscape.match("?test*;thing*"))
    assertTrue(simpleMatchEscape.match("?test*;AAAAAthing*"))
    assertTrue(simpleMatchInvert.match("test"))
    assertTrue(simpleMatchInvert.match("testing things"))
    // Assert implicit wildcard succeeds
    assertTrue(simpleMatchImplicit.match("AAAAAA"))
    // Assert partial match fails
    assertFalse(simpleMatch.match("test"))
    assertFalse(simpleMatch.match("another"))
    assertFalse(simpleMatch.match("anotherBB"))
    // Assert unrelated fails
    assertFalse(simpleMatch.match("not above"))
    // Assert escaped wildcard fails
    assertFalse(simpleMatchEscape.match("@testing"))
    // Assert inverted match fails
    assertFalse(simpleMatchInvert.match("testing"))
    assertFalse(simpleMatchImplicit.match("testing"))

    // Assert case sensitivity followed
    assertFalse(simpleMatch.caseSensitive)
    assertTrue(simpleMatch.match("@TeSt"))
    assertTrue(simpleMatchCS.caseSensitive)
    assertFalse(simpleMatchCS.match("@TeSt"))

    // Assert complex match
    for (normMatch in complexMatchNormal) {
      // Each normal component should match
      assertTrue(complexMatch.match(normMatch))
    }

    for (invertMatch in complexMatchInvert) {
      // Each invert component should not match
      assertFalse(complexMatch.match(invertMatch))
    }

    // Assert complex not literal match
    assertFalse(complexMatch.match(complexMatchFull))
    // Assert complex unrelated not match
    assertFalse(complexMatch.match("other"))
  }

  @Test
  fun matchRegEx() {
    val emptyMatch =
      ExpressionMatch(
        """ """,
        ExpressionMatch.MatchMode.MatchRegEx, false
      )
    // Simple regex, case-insensitive
    val simpleMatch =
      ExpressionMatch(
        """simple.\*escape-match.*""",
        ExpressionMatch.MatchMode.MatchRegEx, false
      )
    // Simple regex, case-sensitive
    val simpleMatchCS =
      ExpressionMatch(
        """simple.\*escape-match.*""",
        ExpressionMatch.MatchMode.MatchRegEx, true
      )
    // Inverted regex, case-insensitive
    val simpleMatchInvert =
      ExpressionMatch(
        """!invert.\*escape-match.*""",
        ExpressionMatch.MatchMode.MatchRegEx, false
      )
    // Non-inverted regex, case-insensitive
    val simpleMatchNoInvert =
      ExpressionMatch(
        """\!simple.\*escape-match.*""",
        ExpressionMatch.MatchMode.MatchRegEx, false
      )
    // Non-inverted regex literal slash, case-insensitive
    val simpleMatchNoInvertSlash =
      ExpressionMatch(
        """\\!simple.\*escape-match.*""",
        ExpressionMatch.MatchMode.MatchRegEx, false
      )

    // Assert valid and not empty
    assertTrue(emptyMatch.isEmpty())
    assertFalse(simpleMatch.isEmpty())
    assertFalse(simpleMatchCS.isEmpty())
    assertFalse(simpleMatchInvert.isEmpty())
    assertFalse(simpleMatchNoInvert.isEmpty())
    assertFalse(simpleMatchNoInvertSlash.isEmpty())

    // Assert match succeeds
    assertTrue(simpleMatch.match("simpleA*escape-match"))
    assertTrue(simpleMatch.match("simpleA*escape-matchBBBB"))
    assertTrue(simpleMatchInvert.match("not above"))
    assertTrue(simpleMatchNoInvert.match("!simpleA*escape-matchBBBB"))
    assertTrue(simpleMatchNoInvertSlash.match("""\!simpleA*escape-matchBBBB"""))
    // Assert partial match fails
    assertFalse(simpleMatch.match("simpleA*escape-mat"))
    assertFalse(simpleMatch.match("simple*escape-match"))
    // Assert unrelated fails
    assertFalse(simpleMatch.match("not above"))
    // Assert escaped wildcard fails
    assertFalse(simpleMatch.match("simpleABBBBescape-matchBBBB"))
    // Assert inverted fails
    assertFalse(simpleMatchInvert.match("invertA*escape-match"))
    assertFalse(simpleMatchInvert.match("invertA*escape-matchBBBB"))
    assertFalse(simpleMatchNoInvert.match("simpleA*escape-matchBBBB"))
    assertFalse(simpleMatchNoInvert.match("anything"))
    assertFalse(simpleMatchNoInvertSlash.match("!simpleA*escape-matchBBBB"))
    assertFalse(simpleMatchNoInvertSlash.match("anything"))

    // Assert case sensitivity followed
    assertFalse(simpleMatch.caseSensitive)
    assertTrue(simpleMatch.match("SiMpLEA*escape-MATCH"))
    assertTrue(simpleMatchCS.caseSensitive)
    assertFalse(simpleMatchCS.match("SiMpLEA*escape-MATCH"))
  }

  @Test
  fun testInvalid() {
    val invalidRegex = ExpressionMatch("*network", ExpressionMatch.MatchMode.MatchRegEx, false)
    assertFalse(invalidRegex.match(""))
    assertFalse(invalidRegex.match("network"))
    assertFalse(invalidRegex.match("testnetwork"))
  }

  // Tests imported from https://github.com/ircdocs/parser-tests/blob/master/tests/mask-match.yaml
  @Test
  fun testDan() {
    val mask1 = ExpressionMatch(
      "*@127.0.0.1",
      ExpressionMatch.MatchMode.MatchWildcard,
      caseSensitive = false
    )
    assertTrue(mask1.match("coolguy!ab@127.0.0.1"))
    assertTrue(mask1.match("cooldud3!~bc@127.0.0.1"))
    assertFalse(mask1.match("coolguy!ab@127.0.0.5"))
    assertFalse(mask1.match("cooldud3!~d@124.0.0.1"))

    val mask2 = ExpressionMatch(
      "cool*@*",
      ExpressionMatch.MatchMode.MatchWildcard,
      caseSensitive = false
    )
    assertTrue(mask2.match("coolguy!ab@127.0.0.1"))
    assertTrue(mask2.match("cooldud3!~bc@127.0.0.1"))
    assertTrue(mask2.match("cool132!ab@example.com"))
    assertFalse(mask2.match("koolguy!ab@127.0.0.5"))
    assertFalse(mask2.match("cooodud3!~d@124.0.0.1"))

    val mask3 = ExpressionMatch(
      "cool!*@*",
      ExpressionMatch.MatchMode.MatchWildcard,
      caseSensitive = false
    )
    assertTrue(mask3.match("cool!guyab@127.0.0.1"))
    assertTrue(mask3.match("cool!~dudebc@127.0.0.1"))
    assertTrue(mask3.match("cool!312ab@example.com"))
    assertFalse(mask3.match("coolguy!ab@127.0.0.1"))
    assertFalse(mask3.match("cooldud3!~bc@127.0.0.1"))
    assertFalse(mask3.match("koolguy!ab@127.0.0.5"))
    assertFalse(mask3.match("cooodud3!~d@124.0.0.1"))

    // Cause failures in fnmatch/glob based matchers
    val mask4 = ExpressionMatch(
      "cool[guy]!*@*",
      ExpressionMatch.MatchMode.MatchWildcard,
      caseSensitive = false
    )
    assertTrue(mask4.match("cool[guy]!guy@127.0.0.1"))
    assertTrue(mask4.match("cool[guy]!a@example.com"))
    assertFalse(mask4.match("coolg!ab@127.0.0.1"))
    assertFalse(mask4.match("cool[!ac@127.0.1.1"))
  }
}
