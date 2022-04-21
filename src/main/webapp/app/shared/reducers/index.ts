import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import hrInterview from 'app/entities/hr-interview/hr-interview.reducer';
// prettier-ignore
import technicalInterview from 'app/entities/technical-interview/technical-interview.reducer';
// prettier-ignore
import process from 'app/entities/process/process.reducer';
// prettier-ignore
import candidate from 'app/entities/candidate/candidate.reducer';
// prettier-ignore
import file from 'app/entities/file/file.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  hrInterview,
  technicalInterview,
  process,
  candidate,
  file,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
