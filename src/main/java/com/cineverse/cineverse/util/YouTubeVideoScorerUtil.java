package com.cineverse.cineverse.util;

import com.cineverse.cineverse.infrastructure.youtube.Video;

import java.util.*;

public class YouTubeVideoScorerUtil {

    private static final int TRUSTED_CHANNEL_SCORE = 10;
    private static final int OFFICIAL_TRAILER_COMBO_SCORE = 8;
    private static final int EXACT_YEAR_MATCH_SCORE = 12;
    private static final int POSITIVE_KEYWORD_SCORE = 4;
    private static final int APPROXIMATE_YEAR_MATCH_SCORE = 3;
    private static final int TYPE_MATCH_SCORE = 2;
    private static final int NEGATIVE_KEYWORD_PENALTY = -8;
    private static final int WRONG_YEAR_PENALTY = -20;
    private static final int SEQUEL_PREQUEL_PENALTY = -15;
    private static final int FAKE_TRAILER_PENALTY = -15;

    private static final Set<String> POSITIVE_KEYWORDS = Set.of(
            "official trailer", "movie trailer", "series trailer", "official promo",
            "first look", "exclusive trailer", "international trailer", "final trailer",
            "main trailer", "new trailer", "latest trailer", "movie promo", "series promo", "official clip",
            "الاعلان الرسمي", "التريلر الرسمي", "اعلان تشويقي", "اعلان رسمي",
            "تريلر رسمي", "عرض اول", "حصري", "اعلان حصري", "برومو رسمي",
            "اعلان فيلم", "اعلان مسلسل",
            "anime", "tvアニメ", "アニメ", "pv", "trailer", "teaser"
    );

    private static final Set<String> NEGATIVE_KEYWORDS = Set.of(
            "fan trailer", "fanmade", "fan made", "fan edit", "fan video",
            "tribute", "parody", "spoof", "fake", "leaked", "bootleg",
            "unofficial", "concept", "custom", "reimagined",
            "reaction", "review", "analysis", "breakdown", "explained",
            "easter eggs", "theory", "prediction", "speculation",
            "vs", "versus", "comparison", "compare", "battle",
            "behind the scenes", "making of", "bloopers", "deleted scenes",
            "interview", "cast", "director", "producer",
            "amv", "music video", "soundtrack", "theme song", "opening",
            "ending", "ost", "cover", "remix", "mashup",
            "tiktok", "instagram", "reels", "shorts", "compilation",
            "meme", "funny", "comedy", "sketch",
            "cam rip", "cinema recording", "phone recording", "pirated",
            "low quality", "potato quality", "leaked footage",
            "رد فعل", "تحليل", "مقارنة", "تعديل", "تسريب", "مشهد",
            "خلف الكواليس", "فيديو المعجبين", "نسخة المعجبين",
            "اغنية", "ريمكس", "تيك توك", "اعادة", "تصميم", "بوستر",
            "تتر", "مضحك", "كوميدي", "ساخر", "الفنان", "الفنانة", "يروي",
            "تروي", "حفلة", "مقابلة", "مقابلات", "كواليس", "فيديوهات", "فيديو"
    );

    private static final Set<String> FAKE_INDICATORS = Set.of(
            "fake trailer", "concept trailer", "fan concept", "what if",
            "alternate", "director's cut concept", "unreleased concept",
            "مفهوم", "وهمي", "تخيلي", "غير رسمي"
    );

    private static final Set<String> SEQUEL_PREQUEL_INDICATORS = Set.of(
            "sequel", "prequel", "remake", "reboot", "reimagined", "returns",
            "part 2", "part ii", "part 3", "part iii", "part iv", "part v",
            "chapter 2", "chapter ii", "chapter 3", "chapter iii",
            "الجزء الثاني", "الجزء الثالث", "العودة", "جديد"
    );

    private static final Set<String> TYPE_KEYWORDS = Set.of(
            "trailer", "teaser", "promo", "preview", "clip", "official",
            "movie", "film", "series", "season", "anime", "show",
            "اعلان", "تريلر", "رسمي", "فيلم", "الموسم", "مسلسل",
            "تشويقي", "حصري", "عرض", "برومو"
    );

    private static final Set<String> TRUSTED_CHANNELS = Set.of(
            "netflix", "netflix anime", "disney plus", "disney", "amazon prime video",
            "hulu", "hbo max", "paramount plus", "apple tv", "crunchyroll",
            "funimation", "viz", "muse asia", "aniplex usa", "trailersplaygroundhd",
            "warner bros. pictures", "universal pictures", "sony pictures entertainment",
            "paramount pictures", "20th century studios", "marvel entertainment",
            "dc entertainment", "pixar", "dreamworks animation", "illumination",
            "a24", "lionsgate", "mgm", "columbia pictures", "focus features",
            "searchlight pictures", "new line cinema", "legendary entertainment",
            "yrf", "amazon mgm studios", "sony pictures classics", "dc",
            "walt disney studios", "disney junior", "one media", "warner bros.",
            "disney channel", "cartoon network", "adult swim", "fox", "cw",
            "freeform", "fx networks", "amc", "showtime", "starz", "rotten tomatoes classic trailers",
            "toei animation", "studio ghibli", "madhouse", "bones", "mappa",
            "wit studio", "kyoto animation", "a-1 pictures", "pierrot",
            "mbc", "shahid", "rotana", "watchit", "al nahar", "cbc",
            "on drama", "dmc", "sbc", "abudhabi tv", "alkass", "alarabiya",
            "tvision", "orient films - أفلام أورينت", "stars media",
            "mohamed ramadan i محمد رمضان", "art", "lbc", "el sobky production السبكي للإنتاج الفني",
            "elsobky series السبكي مسلسلات", "viu mena", "viu saudi", "viu egypt", "egyads | إعلان مصر",
            "magnum productions", "cedars art production", "hashtag-هاشتاج", "film square", "raw entertainment",
            "arm arts", "new century production", "film clinic", "synergy films", "conquer production", "front row " +
                    "filmed entertainment",
            "united bros. studios", "chiaroscuro film productions", "oscar movie-فيلم أوسكار", "السبكي للانتاج " +
                    "السينمائي - ELSOBKY",
            "ms productions", "nilewood productions", "alarabiacinema", "stc tv", "misr international films", "taher " +
                    "media production",
            "hush films", "algorio film production", "mbaproductionegypt", "beelinktv", "tamer hosny"
    );

    public int score(Video video, String searchTitle, int searchYear) {
        if (video == null || searchTitle == null) {
            return 0;
        }
        String videoTitle = normalize(video.getTitle());
        String channelName = normalize(video.getChannelTitle());
        String description = normalize(video.getDescription());
        String normalizedSearchTitle = normalize(searchTitle);
        int score = 0;

        if (containsAnyKeyword(videoTitle, FAKE_INDICATORS) ||
                containsAnyKeyword(description, FAKE_INDICATORS)) {
            score += FAKE_TRAILER_PENALTY;
        }
        score += checkTrustedChannel(channelName);

        if (videoTitle.contains("official") && videoTitle.contains("trailer") ||
                (videoTitle.contains("اعلان") && videoTitle.contains("رسمي"))) {
            score += OFFICIAL_TRAILER_COMBO_SCORE;
        }

        score += checkEnhancedYearMatch(videoTitle, description, searchYear);
        score += checkSequelPrequelPenalty(videoTitle, searchYear);
        score += checkKeywords(videoTitle, POSITIVE_KEYWORDS, POSITIVE_KEYWORD_SCORE);
        score += checkKeywords(videoTitle, NEGATIVE_KEYWORDS, NEGATIVE_KEYWORD_PENALTY);
        score += checkTypeKeywords(videoTitle, normalizedSearchTitle);

        if (channelName.contains("fan") || channelName.contains("unofficial") ||
                channelName.contains("concept") || channelName.contains("fake")) {
            score -= 5;
        }

        if (!hasAnyTitleKeywordMatch(videoTitle, normalizedSearchTitle)) {
            score = 0;
        }

        return Math.max(0, score);
    }

    private boolean hasAnyTitleKeywordMatch(String videoTitle, String searchTitle) {
        if (videoTitle == null || searchTitle == null) {
            return false;
        }
        String[] searchWords = searchTitle.split("\\s+");
        for (String word : searchWords) {
            if (word.length() >= 3 && videoTitle.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean containsAnyKeyword(String text, Set<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }

    private int checkTrustedChannel(String channelName) {
        for (String trustedChannel : TRUSTED_CHANNELS) {
            if (channelName.equals(trustedChannel)) {
                return TRUSTED_CHANNEL_SCORE;
            }
        }
        return 0;
    }

    private int checkEnhancedYearMatch(String videoTitle, String description, int searchYear) {
        String searchYearStr = String.valueOf(searchYear);
        boolean titleHasYear = videoTitle.contains(searchYearStr);
        boolean descHasYear = description.contains(searchYearStr);

        if (titleHasYear) {
            return EXACT_YEAR_MATCH_SCORE;
        }

        if (descHasYear) {
            return APPROXIMATE_YEAR_MATCH_SCORE;
        }

        int currentYear = java.time.Year.now().getValue();
        for (int year = 1980; year <= currentYear; year++) {
            if (year != searchYear && (videoTitle.contains(String.valueOf(year)) || description.contains(String.valueOf(year)))) {
                if (year >= currentYear - 2 && (videoTitle.contains("trailer") || videoTitle.contains("teaser"))) {
                    continue;
                }
                return WRONG_YEAR_PENALTY;
            }
        }

        return 0;
    }

    private int checkSequelPrequelPenalty(String videoTitle, int searchYear) {
        for (String indicator : SEQUEL_PREQUEL_INDICATORS) {
            if (videoTitle.contains(indicator)) {
                int currentYear = java.time.Year.now().getValue();
                boolean isRecentTrailer = videoTitle.contains(String.valueOf(currentYear)) ||
                        videoTitle.contains(String.valueOf(currentYear - 1));

                if (searchYear < 2020 && isRecentTrailer) {
                    return SEQUEL_PREQUEL_PENALTY;
                }
            }
        }
        return 0;
    }

    private int checkKeywords(String text, Set<String> keywords, int scorePerMatch) {
        int totalScore = 0;
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                totalScore += scorePerMatch;
            }
        }
        return totalScore;
    }

    private int checkTypeKeywords(String videoTitle, String searchTitle) {
        int score = 0;
        String[] searchWords = searchTitle.split("\\s+");

        for (String word : searchWords) {
            if (word.length() >= 3 && TYPE_KEYWORDS.contains(word) && videoTitle.contains(word)) {
                score += TYPE_MATCH_SCORE;
            }
        }
        return score;
    }
}