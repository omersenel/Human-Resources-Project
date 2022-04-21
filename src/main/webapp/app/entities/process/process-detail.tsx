import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './process.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProcessDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const processEntity = useAppSelector(state => state.process.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="processDetailsHeading">
          <Translate contentKey="iKaMetApp.process.detail.title">Process</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{processEntity.id}</dd>
          <dt>
            <span id="pdate">
              <Translate contentKey="iKaMetApp.process.pdate">Pdate</Translate>
            </span>
          </dt>
          <dd>{processEntity.pdate ? <TextFormat value={processEntity.pdate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="department">
              <Translate contentKey="iKaMetApp.process.department">Department</Translate>
            </span>
          </dt>
          <dd>{processEntity.department}</dd>
          <dt>
            <span id="technicalIndicators">
              <Translate contentKey="iKaMetApp.process.technicalIndicators">Technical Indicators</Translate>
            </span>
          </dt>
          <dd>{processEntity.technicalIndicators}</dd>
          <dt>
            <span id="experience">
              <Translate contentKey="iKaMetApp.process.experience">Experience</Translate>
            </span>
          </dt>
          <dd>{processEntity.experience}</dd>
          <dt>
            <span id="position">
              <Translate contentKey="iKaMetApp.process.position">Position</Translate>
            </span>
          </dt>
          <dd>{processEntity.position}</dd>
          <dt>
            <span id="salaryExpectation">
              <Translate contentKey="iKaMetApp.process.salaryExpectation">Salary Expectation</Translate>
            </span>
          </dt>
          <dd>{processEntity.salaryExpectation}</dd>
          <dt>
            <span id="possibleAssignmnet">
              <Translate contentKey="iKaMetApp.process.possibleAssignmnet">Possible Assignmnet</Translate>
            </span>
          </dt>
          <dd>{processEntity.possibleAssignmnet}</dd>
          <dt>
            <span id="lastStatus">
              <Translate contentKey="iKaMetApp.process.lastStatus">Last Status</Translate>
            </span>
          </dt>
          <dd>{processEntity.lastStatus}</dd>
          <dt>
            <span id="lastDescription">
              <Translate contentKey="iKaMetApp.process.lastDescription">Last Description</Translate>
            </span>
          </dt>
          <dd>{processEntity.lastDescription}</dd>
          <dt>
            <span id="editBy">
              <Translate contentKey="iKaMetApp.process.editBy">Edit By</Translate>
            </span>
          </dt>
          <dd>{processEntity.editBy}</dd>
          <dt>
            <Translate contentKey="iKaMetApp.process.candidate">Candidate</Translate>
          </dt>
          <dd>{processEntity.candidate ? processEntity.candidate.firstName : ''}{processEntity.candidate ? processEntity.candidate.lastName : ''}</dd>

        </dl>
        <Button tag={Link} to="/process" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/process/${processEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProcessDetail;
