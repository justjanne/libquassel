/*
 * libquassel
 * Copyright (c) 2021 Janne Mareike Koschinski
 * Copyright (c) 2021 The Quassel Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 */

package de.justjanne.libquassel.protocol.util.irc

object IrcISupport {
  /**
   * Indicates the maximum number of online nicknames a user may have in their accept list.
   *
   * Format: `ACCEPT=<number>`
   *
   * Examples:
   * - `ACCEPT=20`
   */
  const val ACCEPT = "ACCEPT"

  /**
   * Indicates the maximum length of an away message. If "number" is not defined, there is no limit.
   *
   * Format: `AWAYLEN=\[number]`
   *
   * Examples:
   * - `AWAYLEN`
   * - `AWAYLEN=8`
   */
  const val AWAYLEN = "AWAYLEN"

  /**
   * Indicates that the "caller-id" user mode is supported, which rejects messages from unauthorized users.
   * "letter" defines the mode character, which is used for this feature.
   * If the value is not given, it defaults to the mode "g".
   *
   * Format: `CALLERID\[=letter]`
   *
   * Examples:
   * - `CALLERID`
   * - `CALLERID=g`
   */
  const val CALLERID = "CALLERID"

  /**
   * Indicates the method that’s used to compare equality of case-insensitive strings (such as nick/channel names).
   * Typical values include "ascii" and "rfc1459".
   *
   * Format: `CASEMAPPING=<string>`
   *
   * Examples:
   * - `CASEMAPPING=rfc1459`
   */
  const val CASEMAPPING = "CASEMAPPING"

  /**
   * Indicates the maximum number of channels a client may join.
   * Though a client shouldn’t assume that other clients are limited to what they receive here.
   * If "number" is empty, there is no limit.
   *
   * Format: `CHANLIMIT=<prefix:[number[,prefix:number[,...]]]> `
   *
   * Examples:
   * - `CHANLIMIT=#+:25,&:`
   * - `CHANLIMIT=&#:50`
   */
  const val CHANLIMIT = "CHANLIMIT"

  /**
   * Indicates the channel modes available and which types of arguments they do or do not take.
   *
   * Also see: [MAXLIST]
   *
   * Format: `CHANMODES=<A>,<B>,<C>,<D>`
   *
   * Examples:
   * - `CHANMODES=b,k,l,imnpst`
   * - `CHANMODES=beI,k,l,BCMNORScimnpstz`
   */
  const val CHANMODES = "CHANMODES"

  /**
   * Specifies the maximum length of a channel name that clients may join.
   *
   * Format: `CHANNELLEN=<number>`
   *
   * Examples:
   * - `CHANNELLEN=50`
   */
  const val CHANNELLEN = "CHANNELLEN"

  /**
   * Indicates the types of channels supported on this server.
   * These are channel type prefixes as specified in the RFC.
   *
   * Format: `CHANTYPES=\[string\]`
   *
   * Examples:
   * - `CHANTYPES=&#`
   */
  const val CHANTYPES = "CHANTYPES"

  /**
   * Indicates that the server supports the "CNOTICE" command. This is an extension to the "NOTICE" command.
   *
   * Format: `CNOTICE`
   *
   * Examples:
   * - `CNOTICE`
   */
  const val CNOTICE = "CNOTICE"

  /**
   * Indicates that the server supports the "CNOTICE" command. This is an extension to the "NOTICE" command.
   *
   * Format: `CPRIVMSG`
   *
   * Examples:
   * - `CPRIVMSG`
   */
  const val CPRIVMSG = "CPRIVMSG"

  /**
   * Indicates that the server supports the "DEAF" user mode and the given character is used to represent that mode.
   *
   * Format: `DEAF=<letter>`
   *
   * Examples:
   * - `DEAF=D`
   */
  const val DEAF = "DEAF"

  /**
   * Indicates that the server supports search extensions to the "LIST" command.
   *
   * Format: `ELIST=<string>`
   *
   * Examples:
   * - `ELIST=CMNTU`
   */
  const val ELIST = "ELIST"

  /**
   * Indicates that the server supports filtering extensions to the "SILENCE" command.
   * If a value is specified then it contains the supported filter flags.
   *
   * Format: `ESILENCE=\[flags]`
   *
   * Examples:
   * - `ESILENCE`
   * - `ESILENCE=CcdiNnPpTtx`
   */
  const val ESILENCE = "ESILENCE"

  /**
   * Indicates that the server supports the "ETRACE" command (IRC operators-only), which is similar to "TRACE", but only
   * works on nicknames and has a few different options.
   *
   * Format: `ETRACE`
   *
   * Examples:
   * - `ETRACE`
   */
  const val ETRACE = "ETRACE"

  /**
   * Indicates that the server supports “ban exemptions”.
   * The letter is OPTIONAL and defines the mode character which is used for this.
   * When no letter is provided, it defaults to "e".
   *
   * Format: `EXCEPTS[=letter]`
   *
   * Examples:
   * - `EXCEPTS`
   * - `EXCEPTS=e`
   */
  const val EXCEPTS = "EXCEPTS"

  /**
   * Indicates that the server supports “ban exemptions”.
   * The letter is OPTIONAL and defines the mode character which is used for this.
   * When no letter is provided, it defaults to "e".
   *
   * Format: `EXCEPTS[=letter]`
   *
   * Examples:
   * - `EXCEPTS`
   * - `EXCEPTS=e`
   */
  const val EXTBAN = "EXTBAN"

  /**
   * Indicates that the server supports “invite exemptions”.
   * The letter is OPTIONAL and defines the mode character, which is used for this.
   * When no letter is provided, it defaults to "I".
   *
   * Format: `INVEX[=letter]`
   *
   * Examples:
   * - `INVEX`
   * - `INVEX=I`
   */
  const val INVEX = "INVEX"

  /**
   * Indicates the maximum length of a channel key.
   *
   * Format: `KEYLEN=<number>`
   *
   * Examples:
   * - `KEYLEN=23`
   */
  const val KEYLEN = "KEYLEN"

  /**
   * Indicates the maximum length of a kick message. If "number" is not defined, there is no limit.
   *
   * Format: `KICKLEN=\[number]`
   *
   * Examples:
   * - `KICKLEN`
   * - `KICKLEN=180`
   */
  const val KICKLEN = "KICKLEN"

  /**
   * Indicates support for the "KNOCK" command, which is used to request an invite to a channel.
   *
   * Format: `KNOCK`
   *
   * Examples:
   * - `KNOCK`
   */
  const val KNOCK = "KNOCK"

  /**
   * Indicates how many “variable” modes of "type A" that have been defined in the "CHANMODES" token a client may set in
   * total on a channel. The value MUST be specified and is a set of "mode:number" pairs, where "mode" is a number of
   * "type A" modes that have been defined in "CHANMODES" and "number" is how many of this mode may be set.
   *
   * Also see: [CHANMODES]
   *
   * Format: `MAXLIST=<mode:number[,mode:number[,...]]>`
   *
   * Examples:
   * - `MAXLIST=beI:25`
   * - `MAXLIST=b:25,eI:50`
   */
  const val MAXLIST = "MAXLIST"

  /**
   * Indicates the maximum length of a nickname that a client may use.
   * Other clients on the network may have nicknames longer than this.
   *
   * Format: `MAXNICKLEN=<number>`
   *
   * Examples:
   * - `MAXNICKLEN=9`
   * - `MAXNICKLEN=32`
   */
  const val MAXNICKLEN = "MAXNICKLEN"

  /**
   * Indicates the maximum number of targets for the "PRIVMSG" & "NOTICE" commands.
   *
   * Also see: [TARGMAX]
   *
   * Format: `MAXTARGETS=<number>`
   *
   * Examples:
   * - `MAXTARGETS=8`
   */
  const val MAXTARGETS = "MAXTARGETS"

  /**
   * Indicates the maximum number of keys a user may have in their metadata.
   * If "number" is not specified, there is no limit.
   *
   * Format: `METADATA[=number]`
   *
   * Examples:
   * - `METADATA`
   * - `METADATA=30`
   */
  const val METADATA = "METADATA"

  /**
   * Indicates how many “variable” modes may be set on a channel by a single "MODE" command from a client. A “variable”
   * mode is defined as being a "type A/B/C" mode as defined in the "CHANMODES" token. The value is optional and when
   * not specified indicates that there is NO limit places on “variable” modes.
   *
   * Format: `MODES=\[number]`
   *
   * Examples:
   * - `MODES`
   * - `MODES=3`
   */
  const val MODES = "MODES"

  /**
   * Indicates the maximum number of targets a user may have in their monitor list.
   * If "number" is not specified, there is no limit.
   *
   * Also see: [WATCH]
   *
   * Format: `MONITOR=\[number]`
   *
   * Examples:
   * - `MONITOR`
   * - `MONITOR=6`
   */
  const val MONITOR = "MONITOR"

  /**
   * For INFORMATIONAL PURPOSES ONLY and indicates the name of the IRC network that the client is connected to.
   * A client SHOULD NOT use this value to make assumptions about supported features on the server.
   *
   * Format: `NETWORK=<string>`
   *
   * Examples:
   * - `NETWORK=EFNet`
   * - `NETWORK=Rizon`
   */
  const val NETWORK = "NETWORK"

  /**
   * Indicates the maximum length of a nickname that a client may use.
   * Other clients on the network may have nicknames longer than this.
   *
   * Format: `NICKLEN=<number>`
   *
   * Examples:
   * - `NICKLEN=9`
   * - `NICKLEN=32`
   */
  const val NICKLEN = "NICKLEN"

  /**
   * Indicates the channel membership prefixes available on this server and their order in terms of channel privileges
   * they represent, from highest to lowest. If the value is not specified, then NO channel membership prefixes are
   * supported by this server.
   *
   * Format: `PREFIX=\[(modes)prefixes]`
   *
   * Examples:
   * - `PREFIX=`
   * - `PREFIX=(ov)@+`
   * - `PREFIX=(qaohv)~&@%+`
   */
  const val PREFIX = "PREFIX"

  /**
   * Indicates that the client may request a "LIST" command from the server without being disconnected due to the large
   * amount of data. This token MUST NOT have a value.
   *
   * Format: `SAFELIST`
   *
   * Examples:
   * - `SAFELIST`
   */
  const val SAFELIST = "SAFELIST"

  /**
   * Indicates the maximum number of entries a user may have in their silence list.
   * The value is OPTIONAL and if not specified indicates that there is no limit.
   *
   * The "SILENCE" command seems to vary quite a lot between implementations.
   * Most clients include client-side filter/ignore commands and servers have the "CALLERID" client mode as alternatives
   * to this command.
   *
   * Format: `SILENCE=\[number]`
   *
   * Examples:
   * - `SILENCE`
   * - `SILENCE=15`
   */
  const val SILENCE = "SILENCE"

  /**
   * Indicates that the server supports a method for the client to send a message via the "NOTICE" command to those
   * people on a channel with the specified channel membership prefixes. The value MUST be specified and MUST be a list
   * of prefixes as specified in the "PREFIX" token.
   *
   * Format: `STATUSMSG=<string>`
   *
   * Examples:
   * - `STATUSMSG=@+`
   */
  const val STATUSMSG = "STATUSMSG"

  /**
   * Certain commands from a client MAY contain multiple targets.
   * This token defines the maximum number of targets may be specified on each of these commands.
   * The value is OPTIONAL and is a set of "cmd:number" pairs, where "cmd" refers to the specific command and "number"
   * refers to the limit for this command.
   *
   * If the number is not specified for a particular command, then that command does not have a limit on the maximum
   * number of targets. If the "TARGMAX" parameter is not advertised or a value is not sent then a client SHOULD
   * assume that no commands except the "JOIN" and "PART" commands accept multiple parameters.
   *
   * Also see: [MAXTARGETS]
   *
   * Format: `TARGMAX=[cmd:[number][,cmd:[number][,...]]]`
   *
   * Examples:
   * - `TARGMAX=PRIVMSG:3,WHOIS:1,JOIN:`
   * - `TARGMAX=`
   */
  const val TARGMAX = "TARGMAX"

  /**
   * Indicates the maximum length of a topic that a client may set on a channel.
   * Channels on the network MAY have topics with longer lengths than this.
   *
   * Format: `TOPICLEN=<number>`
   *
   * Examples:
   * - `TOPICLEN=120`
   */
  const val TOPICLEN = "TOPICLEN"

  /**
   * Indicates support for the "USERIP" command, which is used to request the direct IP address of the user with the
   * specified nickname. This might be supported by networks that don’t advertise this token.
   *
   * Format: `USERIP`
   *
   * Examples:
   * - `USERIP`
   */
  const val USERIP = "USERIP"

  /**
   * Indicates the maximum length of an username in octets. If "number" is not specified, there is no limit.
   *
   * Format: `USERLEN=\[number]`
   *
   * Examples:
   * - `USERLEN=`
   * - `USERLEN=12`
   */
  const val USERLEN = "USERLEN"

  /**
   * Indicates that the specified list modes may be larger than the value specified in MAXLIST.
   *
   * Format: `VLIST=<modes>`
   *
   * Examples:
   * - `VLIST=be`
   */
  const val VLIST = "VLIST"

  /**
   * Indicates the maximum number of nicknames a user may have in their watch list.
   * The "MONITOR" command is aimed at being a more consistent alternative to this command.
   *
   * Format: `WATCH=<number>`
   *
   * Examples:
   * - `WATCH=100`
   */
  const val WATCH = "WATCH"

  /**
   * Indicates that the server supports extended syntax of the "WHO" command.
   *
   * Format: `WHOX`
   *
   * Examples:
   * - `WHOX`
   */
  const val WHOX = "WHOX"
}
