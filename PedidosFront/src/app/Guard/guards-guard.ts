import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const guardsGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const username = localStorage.getItem('username');
  const password = localStorage.getItem('password');
  
  if (username && password) {
    return true;
  }
  
  router.navigate(['login']);

  return false;
};
