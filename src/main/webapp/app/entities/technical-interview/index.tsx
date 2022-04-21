import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TechnicalInterview from './technical-interview';
import TechnicalInterviewDetail from './technical-interview-detail';
import TechnicalInterviewUpdate from './technical-interview-update';
import TechnicalInterviewDeleteDialog from './technical-interview-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TechnicalInterviewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TechnicalInterviewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TechnicalInterviewDetail} />
      <ErrorBoundaryRoute path={match.url} component={TechnicalInterview} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TechnicalInterviewDeleteDialog} />
  </>
);

export default Routes;
