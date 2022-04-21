import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProcess } from 'app/shared/model/process.model';
import { getEntities as getProcesses } from 'app/entities/process/process.reducer';
import { getEntity, updateEntity, createEntity, reset } from './hr-interview.reducer';
import { IHrInterview } from 'app/shared/model/hr-interview.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const HrInterviewUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const processes = useAppSelector(state => state.process.entities);
  const hrInterviewEntity = useAppSelector(state => state.hrInterview.entity);
  const loading = useAppSelector(state => state.hrInterview.loading);
  const updating = useAppSelector(state => state.hrInterview.updating);
  const updateSuccess = useAppSelector(state => state.hrInterview.updateSuccess);
  const account = useAppSelector(state => state.authentication.account);
  const handleClose = () => {
    props.history.push('/hr-interview');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProcesses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...hrInterviewEntity,
      ...values,
      process: processes.find(it => it.id.toString() === values.process.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
          loginu: account.login,
        }
      : {
          ...hrInterviewEntity,
          date: convertDateTimeFromServer(hrInterviewEntity.date),
          process: hrInterviewEntity?.process?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="iKaMetApp.hrInterview.home.createOrEditLabel" data-cy="HrInterviewCreateUpdateHeading">
            <Translate contentKey="iKaMetApp.hrInterview.home.createOrEditLabel">Create or edit a HrInterview</Translate>
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
                  id="hr-interview-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('iKaMetApp.hrInterview.date')}
                id="hr-interview-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('iKaMetApp.hrInterview.score')}
                id="hr-interview-score"
                name="score"
                data-cy="score"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.hrInterview.ikStatus')}
                id="hr-interview-ikStatus"
                name="ikStatus"
                data-cy="ikStatus"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.hrInterview.notes')}
                id="hr-interview-notes"
                name="notes"
                data-cy="notes"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.hrInterview.editBy')}
                id="technical-interview-editBy"
                name="editBy"
                data-cy="editBy"
                type="select"

              >
                <option value={account.login} key={account.login}>
                  {account.login}
                </option>
              </ValidatedField>
              <ValidatedField
                label={translate('iKaMetApp.hrInterview.process')}
                id="hr-interview-process"
                name="process"
                data-cy="process"
                type="select"
              >
                <option value="" key="0" />
                {processes
                  ? processes.filter(ide=>ide.lastStatus === "" ).map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.candidate.firstName}{otherEntity.candidate.lastName}
                    </option>
                  ))
                  : null}
              </ValidatedField>

              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hr-interview" replace color="info">
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

export default HrInterviewUpdate;
