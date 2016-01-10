import java.awt.Rectangle;

public class Balle {

	double x;
	double y;
	int largeur = 8;
	int hauteur = 4;

	Entite e;

	double direction = 15;

	int force = 20;

	Rectangle hitbox;

	public Balle(Entite e) {
		this.e = e;
		if (e instanceof Bandit || e instanceof Personnage) {
			if (e.spriteActuel.contains("droite")) {
				x = e.x + 70;
				y = e.y + 30;
			} else if (e.spriteActuel.contains("gauche")) {
				x = e.x - 10;
				y = e.y + 30;
				direction = -direction;
			}
		}
		hitbox = new Rectangle((int) x, (int) y, largeur, hauteur);
	}

	public void deplacer() {
		x += direction;
		force--;
		if (force <= 0) {
			e.ballesSupr.add(this);
		}
		hitbox.x = (int) x;
	}

	public void suprimmer() {
		e.balles.remove(this);
	}

}
