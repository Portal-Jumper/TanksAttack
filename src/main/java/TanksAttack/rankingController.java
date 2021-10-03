package TanksAttack;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.ArrayList;

public class rankingController {

    @FXML

    Label ranking;

    public void initialize() throws SQLException, ClassNotFoundException {
        ranking();
    }

    private void ranking() throws SQLException, ClassNotFoundException {

        ArrayList<ArrayList> list = DatabaseConnection.getTenBestPlayers();
        ArrayList<String> login = list.get(0);
        ArrayList<String> points = list.get(1);

        String info = String.format("Player %s scored %s points\n Player %s scored %s points\n Player %s scored %s " +
                "points\n Player %s scored %s points\n Player %s scored %s points\n Player %s scored %s points\n " +
                "Player %s scored %s points\n Player %s scored %s points\n Player %s scored %s points\n Player %s " +
                "scored %s points\n", login.get(0), points.get(0), login.get(1), points.get(1), login.get(2),
                points.get(2), login.get(3), points.get(3), login.get(4), points.get(4), login.get(5), points.get(5),
                login.get(6), points.get(6), login.get(7), points.get(7), login.get(8), points.get(8),login.get(9),
                points.get(9));

        ranking.setText(info);
    }


}
