import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarRef } from "@angular/material/snack-bar";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {

  constructor(private snackBar: MatSnackBar) { }

  public openSnackBar(message: string, action: string, duration: number): Observable<void> {
    const snackbarRef: MatSnackBarRef<unknown> = this.snackBar.open(message, action, {
      duration,
    });
    return snackbarRef.onAction();
  }
}
