import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './candidate.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CandidateDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const candidateEntity = useAppSelector(state => state.candidate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="candidateDetailsHeading">
          <Translate contentKey="iKaMetApp.candidate.detail.title">Candidate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{candidateEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="iKaMetApp.candidate.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{candidateEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="iKaMetApp.candidate.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{candidateEntity.lastName}</dd>
          <dt>
            <span id="university">
              <Translate contentKey="iKaMetApp.candidate.university">University</Translate>
            </span>
          </dt>
          <dd>{candidateEntity.university}</dd>
          <dt>
            <span id="graduationYear">
              <Translate contentKey="iKaMetApp.candidate.graduationYear">Graduation Year</Translate>
            </span>
          </dt>
          <dd>{candidateEntity.graduationYear}</dd>
          <dt>
            <span id="gpa">
              <Translate contentKey="iKaMetApp.candidate.gpa">Gpa</Translate>
            </span>
          </dt>
          <dd>{candidateEntity.gpa}</dd>
          <dt>
            <span id="editBy">
              <Translate contentKey="iKaMetApp.candidate.editBy">Edit By</Translate>
            </span>
          </dt>
          <dd>{candidateEntity.editBy}</dd>
        </dl>
        <Button tag={Link} to="/candidate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/candidate/${candidateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CandidateDetail;
