package ro.csie.en.g1096s04;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long movieId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "genre")
    private Genre genre;

    @ColumnInfo(name = "release")
    @TypeConverters(DateConverter.class)
    private Date release;

    @ColumnInfo(name = "duration")
    private Integer duration;

    @ColumnInfo(name = "poster")
    private String poster;

    @ColumnInfo(name = "boxoffice")
    @TypeConverters(BooleanConverter.class)
    private Boolean recommended;

    public Movie() {
    }

    protected Movie(Parcel in) {
        title = in.readString();
        release = new Date(in.readLong());
        genre = Genre.valueOf(in.readString());
        if (in.readByte() == 0) {
            duration = null;
        } else {
            duration = in.readInt();
        }
        poster = in.readString();
        recommended = in.readInt() == 0 ? false : true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(release.getTime());
        dest.writeString(genre.toString());
        if (duration == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(duration);
        }
        dest.writeString(poster);
        dest.writeInt(recommended == null ? 0 : (recommended == true ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Movie(String title, Genre genre, Date release, Integer duration) {
        this.title = title;
        this.genre = genre;
        this.release = release;
        this.duration = duration;
    }

    public Movie(String movieTitle, Genre movieGenre, Date movieRelease, int movieDuration, boolean movieRecommended, String moviePoster) {
        this.title = movieTitle;
        this.genre = movieGenre;
        this.release = movieRelease;
        this.duration = movieDuration;
        this.recommended = movieRecommended;
        this.poster = moviePoster;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", genre=" + genre +
                ", release=" + release +
                ", duration=" + duration +
                ", poster='" + poster + '\'' +
                '}';
    }
}
