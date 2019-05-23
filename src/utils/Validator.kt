package linhdo.backend.inface.utils

import java.util.regex.Pattern

object Validator {
    private const val IP_ADDRESS_STRING = (
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))")

    /**
     * Valid UCS characters defined in RFC 3987. Excludes space characters.
     */
    private const val UCS_CHAR = "[" +
            "\u00A0-\uD7FF" +
            "\uF900-\uFDCF" +
            "\uFDF0-\uFFEF" +
            "\uD800\uDC00-\uD83F\uDFFD" +
            "\uD840\uDC00-\uD87F\uDFFD" +
            "\uD880\uDC00-\uD8BF\uDFFD" +
            "\uD8C0\uDC00-\uD8FF\uDFFD" +
            "\uD900\uDC00-\uD93F\uDFFD" +
            "\uD940\uDC00-\uD97F\uDFFD" +
            "\uD980\uDC00-\uD9BF\uDFFD" +
            "\uD9C0\uDC00-\uD9FF\uDFFD" +
            "\uDA00\uDC00-\uDA3F\uDFFD" +
            "\uDA40\uDC00-\uDA7F\uDFFD" +
            "\uDA80\uDC00-\uDABF\uDFFD" +
            "\uDAC0\uDC00-\uDAFF\uDFFD" +
            "\uDB00\uDC00-\uDB3F\uDFFD" +
            "\uDB44\uDC00-\uDB7F\uDFFD" +
            "&&[^\u00A0[\u2000-\u200A]\u2028\u2029\u202F\u3000]]"

    /**
     * Valid characters for IRI label defined in RFC 3987.
     */
    private const val LABEL_CHAR = "a-zA-Z0-9$UCS_CHAR"

    /**
     * Valid characters for IRI TLD defined in RFC 3987.
     */
    private const val TLD_CHAR = "a-zA-Z$UCS_CHAR"

    /**
     * RFC 1035 Section 2.3.4 limits the labels to a maximum 63 octets.
     */
    private const val IRI_LABEL =
        "[$LABEL_CHAR](?:[$LABEL_CHAR\\-]{0,61}[$LABEL_CHAR]){0,1}"

    /**
     * RFC 3492 references RFC 1034 and limits Punycode algorithm output to 63 characters.
     */
    private const val PUNYCODE_TLD = "xn\\-\\-[\\w\\-]{0,58}\\w"

    private const val TLD = "($PUNYCODE_TLD|[$TLD_CHAR]{2,63})"

    private const val HOST_NAME = "($IRI_LABEL\\.)+$TLD"

    private const val DOMAIN_NAME_STR = "($HOST_NAME|$IP_ADDRESS_STRING)"

    private const val PROTOCOL = "(?i:http|https|rtsp)://"

    /* A word boundary or end of input.  This is to stop foo.sure from matching as foo.su */
    private const val WORD_BOUNDARY = "(?:\\b|$|^)"

    private const val USER_INFO =
        ("(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@")

    private const val PORT_NUMBER = "\\:\\d{1,5}"

    private const val PATH_AND_QUERY =
        ("[/\\?](?:(?:[$LABEL_CHAR;/\\?:@&=#~\\-\\.\\+!\\*'\\(\\),_\\$])|(?:%[a-fA-F0-9]{2}))*")

    /**
     * Regular expression pattern to match most part of RFC 3987
     * Internationalized URLs, aka IRIs.
     */
    val WEB_URL: Pattern = Pattern.compile(
        "(((?:$PROTOCOL(?:$USER_INFO)?)?(?:$DOMAIN_NAME_STR)(?:$PORT_NUMBER)?)($PATH_AND_QUERY)?$WORD_BOUNDARY)"
    )

    /**
     * Regular expression for valid email characters. Does not include some of the valid characters
     * defined in RFC5321: #&~!^`{}/=$*?|
     */
    private const val EMAIL_CHAR = "$LABEL_CHAR\\+\\-_%'"

    /**
     * Regular expression for local part of an email address. RFC5321 section 4.5.3.1.1 limits
     * the local part to be at most 64 octets.
     */
    private const val EMAIL_ADDRESS_LOCAL_PART =
        "[$EMAIL_CHAR](?:[$EMAIL_CHAR\\.]{0,62}[$EMAIL_CHAR])?"

    /**
     * Regular expression for the domain part of an email address. RFC5321 section 4.5.3.1.2 limits
     * the domain to be at most 255 octets.
     */
    private const val EMAIL_ADDRESS_DOMAIN = "(?=.{1,255}(?:\\s|$|^))$HOST_NAME"


    /**
     * Regular expression pattern to match email addresses. It excludes double quoted local parts
     * and the special characters #&~!^`{}/=$*?| that are included in RFC5321.
     * @hide
     */
    val AUTOLINK_EMAIL_ADDRESS = Pattern.compile(
        "(" + WORD_BOUNDARY +
                "(?:" + EMAIL_ADDRESS_LOCAL_PART + "@" + EMAIL_ADDRESS_DOMAIN + ")" +
                WORD_BOUNDARY + ")"
    )

    val EMAIL_ADDRESS = Pattern.compile(
        ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")
    )

}
