package ro.csie.en.g1096s04;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Movie implements Parcelable {
    private String title;
    private Genre genre;
    private Date release;
    private Integer duration;
    private Byte rating;
    private String poster;
    private Boolean recommended;

    public Movie() {
    }

    public Movie(String title, Genre genre, Date release, Integer duration, Byte rating) {
        this.title = title;
        this.genre = genre;
        this.release = release;
        this.duration = duration;
        this.rating = rating;
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
        if (in.readByte() == 0)
        {
            rating = null;
        }
        else
        {
            rating = in.readByte();
        }
        poster = in.readString();
        recommended = in.readInt() == 1;
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
        if (rating == null)
        {
            dest.writeByte((byte) 0);
        }
        else
        {
            dest.writeByte((byte) 1);
            dest.writeByte(rating);
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
        if (duration == null)
            return 60;
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Byte getRating() {
        if (rating == null)
            return (byte) 0;
        return this.rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", genre=" + genre +
                ", release=" + release +
                ", duration=" + duration +
                ", rating=" + rating +
                ", poster='" + poster + '\'' +
                '}';
    }
}
