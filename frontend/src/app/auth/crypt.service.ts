import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CryptService {

  constructor() { }

  base64ToArrayBuffer(base64: string) {
    const binary_string = window.atob(base64);
    const len = binary_string.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
      bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes.buffer;
  }

  arrayBufferToBase64(buffer: Buffer) {
    let binary = '';
    const bytes = new Uint8Array( buffer );
    const len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode( bytes[ i ] );
    }
    return window.btoa( binary );
  }

  async importPublicKey(spki: string){
    const binaryDer = this.base64ToArrayBuffer(spki);
    return await window.crypto.subtle
      .importKey(
        "spki",
        binaryDer,
        {
          name: 'RSA-OAEP',
          hash: "sha-256",
        },
        false,
        ["encrypt"]
      );
  }

  async encyptData(message: string, cryptoKey: CryptoKey){
    let enc = new TextEncoder();
    let encodedMessage = enc.encode(message);
    const encryptedData = await window.crypto.subtle.encrypt(
      {
        name: "RSA-OAEP"
      },
      cryptoKey,
      encodedMessage
    );
    return this.arrayBufferToBase64(encryptedData);
  }
}
