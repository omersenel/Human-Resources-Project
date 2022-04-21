import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './hr-interview.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getProcesses} from "app/entities/process/process.reducer";

export const HrInterviewDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const processes = useAppSelector(state => state.process.entities);

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(getProcesses({}));
  }, []);

  const hrInterviewEntity = useAppSelector(state => state.hrInterview.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hrInterviewDetailsHeading">
          <Translate contentKey="iKaMetApp.hrInterview.detail.title">HrInterview</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hrInterviewEntity.id}</dd>
          <dt>
            <Translate contentKey="iKaMetApp.hrInterview.process">Process</Translate>
          </dt>
          <dd>{processes
            ? processes.filter(ide =>ide.id === hrInterviewEntity.process.id).map(otherEntity => (

              <option value={hrInterviewEntity.process.id} key={hrInterviewEntity.process.id}>

                {otherEntity.candidate.firstName}{otherEntity.candidate.lastName}{hrInterviewEntity.process ? hrInterviewEntity.process.id : ''}
              </option>
            ))
            : null}
          </dd>

          <dt>
            <span id="score">

              <Translate contentKey="iKaMetApp.hrInterview.score">Score</Translate>
            </span>
          </dt>
          <dd>{hrInterviewEntity.score}</dd>
          <dt>
            <span id="ikStatus">
              <Translate contentKey="iKaMetApp.hrInterview.ikStatus">Ik Status</Translate>
            </span>
          </dt>
          <dd>{hrInterviewEntity.ikStatus}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="iKaMetApp.hrInterview.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hrInterviewEntity.notes}</dd>
          <dt>
            <span id="editBy">
              <Translate contentKey="iKaMetApp.hrInterview.editBy">Edit By</Translate>
            </span>
          </dt>
          <dd>{hrInterviewEntity.editBy}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="iKaMetApp.hrInterview.date">Date</Translate>
            </span>
          </dt>
          <dd>{hrInterviewEntity.date ? <TextFormat value={hrInterviewEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>


        </dl>
        <Button tag={Link} to="/hr-interview" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hr-interview/${hrInterviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HrInterviewDetail;
