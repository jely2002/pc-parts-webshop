import { Injectable } from '@angular/core';
import { UpdateUserRequest, UserModel } from "./user.model";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { PublicKeyResponseModel } from "./public-key-response.model";
import { CryptService } from "./crypt.service";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private httpClient: HttpClient,
    private cryptService: CryptService,
  ) {
  }

  public authenticatedUser: UserModel | null = null;

  getUser(): UserModel {
    return JSON.parse(localStorage.getItem('user') || '{}') as UserModel;
  }

  public checkIfLoggedIn(): Observable<UserModel> {
    return this.httpClient.get<UserModel>(environment.apiUrl + '/auth/user');
  }

  login(email: string, password: string): Promise<UserModel> {
    return new Promise((resolve, reject) => {
      this.httpClient.post<PublicKeyResponseModel>(environment.apiUrl + '/auth/login/initialize', {
        email,
      }).subscribe({
        next: async (response: PublicKeyResponseModel) => {
          const publicKey: CryptoKey = await this.cryptService.importPublicKey(response.publicKey);
          const encryptedPassword = await this.cryptService.encyptData(password, publicKey);
          this.httpClient.post<UserModel>(environment.apiUrl + '/auth/login', {
            email,
            password: encryptedPassword,
          }).subscribe({
            next: (user: UserModel) => {
              this.authenticatedUser = user;
              localStorage.setItem('user', JSON.stringify(user));
              resolve(user);
            },
            error: (error: HttpErrorResponse) => {
              reject(error);
            }
          });
        },
        error: (error: HttpErrorResponse) => {
          reject(error);
        }
      });
    });
  }

  logout(): Observable<void> {
    this.authenticatedUser = null;
    localStorage.clear();
    return this.httpClient.post<void>(environment.apiUrl + '/auth/logout', {logout: true});
  }

  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(environment.apiUrl + '/user/' + id);
  }

  verifyCaptcha(token: string): Observable<void> {
    return this.httpClient.post<void>(environment.apiUrl + '/auth/captcha', token);
  }

  register(email: string, password: string, firstName: string, middleName: string, lastName: string): Promise<UserModel> {
    return new Promise((resolve, reject) => {
      this.httpClient.post<PublicKeyResponseModel>(environment.apiUrl + '/user/initialize', {
        email,
      }).subscribe({
        next: async (response: PublicKeyResponseModel) => {
          const publicKey: CryptoKey = await this.cryptService.importPublicKey(response.publicKey);
          const encryptedPassword = await this.cryptService.encyptData(password, publicKey);
          this.httpClient.post<UserModel>(environment.apiUrl + '/user', {
            email,
            password: encryptedPassword,
            firstName, middleName, lastName,
          }).subscribe({
            next: (user: UserModel) => {
              this.authenticatedUser = user;
              localStorage.setItem('user', JSON.stringify(user));
              resolve(user);
            },
            error: (error: HttpErrorResponse) => {
              reject(error);
            }
          })
        },
        error: (error: HttpErrorResponse) => {
          reject(error);
        }
      })
    });
  }

  updateUser(updateUserRequest: UpdateUserRequest, id: string, email: string): Promise<UserModel> {
    return new Promise((resolve, reject) => {
      this.httpClient.post<PublicKeyResponseModel>(environment.apiUrl + '/auth/login/initialize', {
        email,
      }).subscribe({
        next: async (response: PublicKeyResponseModel) => {
          if (updateUserRequest.password) {
            const publicKey: CryptoKey = await this.cryptService.importPublicKey(response.publicKey);
            updateUserRequest.password = await this.cryptService.encyptData(updateUserRequest.password, publicKey);
          }
          this.httpClient.patch<UserModel>(environment.apiUrl + '/user/' + id, updateUserRequest).subscribe({
            next: (user: UserModel) => {
              this.authenticatedUser = user;
              localStorage.setItem('user', JSON.stringify(user));
              if (updateUserRequest.password || updateUserRequest.email) {
                this.logout();
              }
              resolve(user);
            },
            error: (error: HttpErrorResponse) => {
              reject(error);
            }
          });
        },
        error: (error: HttpErrorResponse) => {
          reject(error);
        }
      })
    });
  }
}
