
import java.nio.charset.StandardCharsets;





/**
 * Disclaimer: 
 * This code is for illustration purposes.
 * Do not use in real-world deployments.
 */
// import AESDemo;

public class PaddingOracleAttackSimulation {

	private static class Sender {
		private byte[] secretKey;
		private String secretMessage = "Top secret!";

		public Sender(byte[] secretKey) {
			this.secretKey = secretKey;
		}

		// This will return both iv and ciphertext
		public byte[] encrypt() {
			return AESDemo.encrypt(secretKey, secretMessage);
		}
	}

	private static class Receiver {
		private byte[] secretKey;

		public Receiver(byte[] secretKey) {
			this.secretKey = secretKey;
		}

		// Padding Oracle (Notice the return type)
		public boolean isDecryptionSuccessful(byte[] ciphertext) {
			return AESDemo.decrypt(secretKey, ciphertext) != null;
		}
	}

	public static class Adversary {

		// This is where you are going to develop the attack
		// Assume you cannot access the key. 
		// You shall not add any methods to the Receiver class.
		// You only have access to the receiver's "isDecryptionSuccessful" only. 
		public String extractSecretMessage(Receiver receiver, byte[] ciphertext) {
			
			byte[] iv = AESDemo.extractIV(ciphertext);
			byte[] ciphertextBlocks = AESDemo.extractCiphertextBlocks(ciphertext);
			boolean result = receiver.isDecryptionSuccessful(AESDemo.prepareCiphertext(iv, ciphertextBlocks));
			System.out.println(result); // This is true initially, as the ciphertext was not altered in any way.
			
			// TODO: WRITE THE ATTACK HERE.
			int i; 
			byte[] iv_copy = iv.clone();
			for (i = 0; i < iv.length; i++) {
				iv_copy[i] = (byte) (iv[i] + 1);  
				result = receiver.isDecryptionSuccessful(AESDemo.prepareCiphertext(iv_copy, ciphertextBlocks));
				if (!result) { 
					break;
				}
			}
			int pad_index = iv.length - i;
            byte[] f_inverse = new byte[iv.length];
			for(i = iv.length- pad_index ; i < iv.length ; i++){
                f_inverse[i] = (byte) (iv[i] ^ pad_index);  
				System.out.println(f_inverse[i]);
			}	
			int j;
			for(i = iv.length- pad_index - 1; i >= 0; i--){
                iv_copy = iv.clone();
				for(j = iv.length- pad_index; j < iv.length; j++){
					iv_copy[j] = (byte) (f_inverse[j] ^ (pad_index + 1));
				}
				for(j = 0; j <= 255; j++){
					iv_copy[i] = (byte) (j ^ (pad_index + 1));
                    result = receiver.isDecryptionSuccessful(AESDemo.prepareCiphertext(iv_copy, ciphertextBlocks));
				    if (result) { 
					    break;
				    }
				}
				f_inverse[i] = (byte) (iv_copy[i] ^ (pad_index + 1));
				pad_index += 1;
			}
			byte[] plaintext = new byte[iv.length]; 
			for(i = 0; i< iv.length - 5; i++){
				plaintext[i] = (byte) (iv[i] ^ f_inverse[i]);
			}
			return new String(plaintext, StandardCharsets.UTF_8);
		}
	}

	public static void main(String[] args) {

		byte[] secretKey = AESDemo.keyGen();
		Sender sender = new Sender(secretKey);
		Receiver receiver = new Receiver(secretKey);
		
		// The adversary does not have the key
		Adversary adversary = new Adversary();

		// Now, let's get some valid encryption from the sender
		byte[] ciphertext = sender.encrypt();

		// The adversary  got the encrypted message from the network.
		// The adversary's goal is to extract the message without knowing the key.
		String message = adversary.extractSecretMessage(receiver, ciphertext);

		System.out.println("Extracted message = " + message);
	}
}