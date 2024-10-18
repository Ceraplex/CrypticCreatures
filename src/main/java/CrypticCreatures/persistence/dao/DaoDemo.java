package CrypticCreatures.persistence.dao;

import CrypticCreatures.core.models.User;
import java.util.Optional;

public class DaoDemo {
    private static Dao<User> dao;

    public static void main(String[] args) {
//        dao = new PlaygroundPointDaoMemory();
        dao = new UsersDaoDb();
        UsersDaoDb.initDb();

        dao.save(new User());
        dao.save(new User());

        User playgroundPoint1 = getPlaygroundPoint(501013);
        System.out.println( playgroundPoint1 );

        dao.update( playgroundPoint1, new String[]
                {"SPIELPLATZPUNKTOGD.fid-339f4d3f_1762344675f_1e5c","501013","POINT (16.365061261939466 48.180602175752234)","Waldmüllerpark","10","\"Basketball, Beachvolleyball, Fußball, Klettern, Reck, Rutschen, Sandspielen, Schaukeln, Seilbahn, Tischtennis, Wippen\"","\"Ballspielkäfig, Ballspielplatz, Kleinkinderspielplatz, Spielplatz\"",""} );
        System.out.println();

        User playgroundPoint2 = getPlaygroundPoint( 501014);
        dao.delete( playgroundPoint2 );
        dao.save(new User());

        dao.getAll().forEach(item -> System.out.println(item));
    }

    private static User getPlaygroundPoint(int id) {
        Optional<User> playgroundPoint = dao.get(id);

        return playgroundPoint.orElseGet(
                () -> new User()
        );
    }
}
