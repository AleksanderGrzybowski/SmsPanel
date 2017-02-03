package smspanel

class MessageSanitizationService {

    static final Map<String, String> POLISH_MAPPINGS = [
            'ż': 'z', 'ó': 'o',
            'ł': 'l', 'ć': 'c',
            'ę': 'e', 'ś': 's',
            'ą': 'a', 'ź': 'z',
            'ń': 'n', 'Ż': 'Z',
            'Ó': 'O', 'Ł': 'L',
            'Ć': 'C', 'Ę': 'E',
            'Ś': 'S', 'Ą': 'A',
            'Ź': 'Z', 'Ń': 'N'
    ]

    // http://stackoverflow.com/questions/8519669/replace-non-ascii-character-fro
    String sanitize(String content) {
        content.split('')
                .collect { it in POLISH_MAPPINGS.keySet() ? POLISH_MAPPINGS[it] : it }
                .join('')
                .replaceAll("\\P{InBasic_Latin}", '')
    }
}
