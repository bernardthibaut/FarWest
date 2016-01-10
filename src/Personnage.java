import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class Personnage extends Entite{

	double gravite = 0.0;
	boolean jumping = false;

	public Personnage() {
		ArrayList<String> nomImages = new ArrayList<>();
		nomImages.add("droite1");
		nomImages.add("droiteMarche1");
		nomImages.add("droiteMarche2");
		nomImages.add("droiteGun1");
		nomImages.add("droiteGun2");
		nomImages.add("gauche1");
		nomImages.add("gaucheMarche1");
		nomImages.add("gaucheMarche2");
		nomImages.add("gaucheGun1");
		nomImages.add("gaucheGun2");

		for (String s : nomImages) {
			Image image = new ImageIcon("res/perso/" + s + ".png").getImage();
			sprites.put(s, image);
		}

	}


	public double getPiedsY() {
		return y + 78;
	}

	public void addGraviteToY() {
		if (getPiedsY() + gravite < 1000) {
			y += gravite;
		} else {
			jumping = false;
		}
	}

}
