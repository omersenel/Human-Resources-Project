import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICandidate } from 'app/shared/model/candidate.model';
import { getEntities as getCandidates } from 'app/entities/candidate/candidate.reducer';
import { getEntity, updateEntity, createEntity, reset } from './file.reducer';
import { IFile } from 'app/shared/model/file.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const candidates = useAppSelector(state => state.candidate.entities);
  const fileEntity = useAppSelector(state => state.file.entity);
  const loading = useAppSelector(state => state.file.loading);
  const updating = useAppSelector(state => state.file.updating);
  const updateSuccess = useAppSelector(state => state.file.updateSuccess);
  const handleClose = () => {
    props.history.push('/file');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCandidates({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...fileEntity,
      ...values,
      candidate: candidates.find(it => it.id.toString() === values.candidate.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...fileEntity,
          candidate: fileEntity?.candidate?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="iKaMetApp.file.home.createOrEditLabel" data-cy="FileCreateUpdateHeading">
            <Translate contentKey="iKaMetApp.file.home.createOrEditLabel">Create or edit a File</Translate>
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
                  id="file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}

              <ValidatedBlobField
                label={translate('iKaMetApp.file.data')}
                id="file-data"
                name="data"
                data-cy="data"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                id="file-candidate"
                name="candidate"
                data-cy="candidate"
                label={translate('iKaMetApp.file.candidate')}
                type="select"
              >
                <option value="" key="0" />
                {candidates
                  ? candidates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.firstName}{otherEntity.lastName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/file" replace color="info">
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

export default FileUpdate;
