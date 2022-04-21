import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HrInterview from './hr-interview';
import TechnicalInterview from './technical-interview';
import Process from './process';
import Candidate from './candidate';
import File from './file';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}hr-interview`} component={HrInterview} />
      <ErrorBoundaryRoute path={`${match.url}technical-interview`} component={TechnicalInterview} />
      <ErrorBoundaryRoute path={`${match.url}process`} component={Process} />
      <ErrorBoundaryRoute path={`${match.url}candidate`} component={Candidate} />
      <ErrorBoundaryRoute path={`${match.url}file`} component={File} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
