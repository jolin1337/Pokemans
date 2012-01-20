package render;

/**
 *
 * @author johannes
 */
public final class ErrorHandler {
	public static class CharacterBoundary{
		static void CharacterOutOfBoundary(){
			System.out.println("\nFocus out of bounds!");
		}
		static void resetCharacterPositionAt(Player c,int posX, int posY){
			System.out.println("We now reset the Character's position");
			c.transport(posY, posY);
		}
	}
}
