package frontline.daybreak.musicplayer

class AudioFile {
    public val fileDir : String

    // ID3 TAG
    public val infoName : String
    public val infoDate : String
    public val infoGenre : String
    public val infoArtist : String
    public val infoAlbum : String
    public val infoDuration : String
    public val infoAlbumArt : String

    constructor(dir    : String, name  : String, date     : String, genre    : String,
                artist : String, album : String, duration : String, albumArt : String)
    {
        fileDir    = dir   ; infoName  = name ; infoDate     = date    ; infoGenre    = genre
        infoArtist = artist; infoAlbum = album; infoDuration = duration; infoAlbumArt = albumArt
    }

}