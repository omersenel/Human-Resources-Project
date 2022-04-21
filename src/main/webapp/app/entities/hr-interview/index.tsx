import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HrInterview from './hr-interview';
import HrInterviewDetail from './hr-interview-detail';
import HrInterviewUpdate from './hr-interview-update';
import HrInterviewDeleteDialog from './hr-interview-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HrInterviewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HrInterviewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HrInterviewDetail} />
      <ErrorBoundaryRoute path={match.url} component={HrInterview} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HrInterviewDeleteDialog} />
  </>
);

export default Routes;
