import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './technical-interview.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getProcesses} from "app/entities/process/process.reducer";

export const TechnicalInterviewDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const processes = useAppSelector(state => state.process.entities);

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(getProcesses({}));
  }, []);

  const technicalInterviewEntity = useAppSelector(state => state.technicalInterview.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="technicalInterviewDetailsHeading">
          <Translate contentKey="iKaMetApp.technicalInterview.detail.title">TechnicalInterview</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{technicalInterviewEntity.id}</dd>

          <dt>
            <Translate contentKey="iKaMetApp.technicalInterview.process">Process</Translate>
          </dt>
          <dd>{processes
            ? processes.filter(ide =>ide.id === technicalInterviewEntity.process.id).map(otherEntity => (

              <option value={technicalInterviewEntity.process.id} key={technicalInterviewEntity.process.id}>

                {otherEntity.candidate.firstName}{otherEntity.candidate.lastName}{technicalInterviewEntity.process ? technicalInterviewEntity.process.id : '1'}
              </option>
            ))
            : null}
          </dd>

          <dt>
            <span id="score">
              <Translate contentKey="iKaMetApp.technicalInterview.score">Score</Translate>
            </span>
          </dt>
          <dd>{technicalInterviewEntity.score}</dd>
          <dt>
            <span id="technicalStatus">
              <Translate contentKey="iKaMetApp.technicalInterview.technicalStatus">Technical Status</Translate>
            </span>
          </dt>
          <dd>{technicalInterviewEntity.technicalStatus}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="iKaMetApp.technicalInterview.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{technicalInterviewEntity.notes}</dd>
          <dt>
            <span id="editBy">
              <Translate contentKey="iKaMetApp.technicalInterview.editBy">Edit By</Translate>
            </span>
          </dt>
          <dd>{technicalInterviewEntity.editBy}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="iKaMetApp.technicalInterview.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {technicalInterviewEntity.date ? (
              <TextFormat value={technicalInterviewEntity.date} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>

        </dl>
        <Button tag={Link} to="/technical-interview" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/technical-interview/${technicalInterviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TechnicalInterviewDetail;
