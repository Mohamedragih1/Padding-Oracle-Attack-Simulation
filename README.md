# Padding Oracle Attack Simulation

This project demonstrates a padding oracle attack simulation, where an adversary is able to recover plaintext from ciphertext without access to the encryption key. By exploiting responses from padding validation, the adversary learns information about the underlying message, illustrating a critical vulnerability in certain encryption schemes.

## Project Overview

The simulation involves three main parties:

1. **Sender**: Encrypts a message using AES with padding and sends the ciphertext to the receiver.
2. **Receiver**: Receives the ciphertext and checks if the padding is valid before decryption.
3. **Adversary**: Intercepts the ciphertext and attempts to reveal the plaintext by sending modified ciphertexts to the receiver and analyzing padding error responses.

Using a padding oracle vulnerability, the adversary extracts the plaintext through repeated queries to the receiver.

## Files

- **`AESDemo.java`**: Provides utility methods for AES encryption and padding. Use this file to understand the encryption process and the structure of the ciphertext.
- **`PaddingOracleAttackSimulation.java`**: Simulates the interaction between the sender, receiver, and adversary. This file includes methods to execute the padding oracle attack by analyzing padding validation responses to extract the plaintext.

## Approach

The project’s attack strategy consists of these steps:

1. **Intercept Ciphertext**: The adversary captures the ciphertext sent from the sender to the receiver.
2. **Modify Ciphertext Blocks**: The adversary modifies specific bytes of the ciphertext and sends them to the receiver, observing if the response indicates valid or invalid padding.
3. **Exploit Padding Responses**: Based on the padding oracle responses, the adversary deduces the plaintext of each block by systematically adjusting the ciphertext until valid padding is achieved.
4. **Recover Plaintext**: The adversary iterates over each byte of each ciphertext block, revealing the full plaintext message by chaining padding oracle responses.
