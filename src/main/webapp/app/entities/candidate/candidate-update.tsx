import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './candidate.reducer';
import { ICandidate } from 'app/shared/model/candidate.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CandidateUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const candidateEntity = useAppSelector(state => state.candidate.entity);
  const loading = useAppSelector(state => state.candidate.loading);
  const updating = useAppSelector(state => state.candidate.updating);
  const updateSuccess = useAppSelector(state => state.candidate.updateSuccess);
  const account = useAppSelector(state => state.authentication.account);
  const handleClose = () => {
    props.history.push('/candidate');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...candidateEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {loginu:account.login,}
      : {

          ...candidateEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="iKaMetApp.candidate.home.createOrEditLabel" data-cy="CandidateCreateUpdateHeading">
            <Translate contentKey="iKaMetApp.candidate.home.createOrEditLabel">Create or edit a Candidate</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="candidate-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('iKaMetApp.candidate.firstName')}
                id="candidate-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.candidate.lastName')}
                id="candidate-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.candidate.university')}
                id="candidate-university"
                name="university"
                data-cy="university"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.candidate.graduationYear')}
                id="candidate-graduationYear"
                name="graduationYear"
                data-cy="graduationYear"
                type="text"
              />
              <ValidatedField label={translate('iKaMetApp.candidate.gpa')} id="candidate-gpa" name="gpa" data-cy="gpa" type="text" />
              <ValidatedField
                label={translate('iKaMetApp.candidate.editBy')}
                id="candidate-editBy"
                name="editBy"
                data-cy="editBy"
                type="select"

              >
                <option value={account.login} key={account.login}>
                  {account.login}
                </option>
              </ValidatedField>

              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/candidate" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CandidateUpdate;
