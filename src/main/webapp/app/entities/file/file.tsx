import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button,  Table } from 'reactstrap';
import { openFile,  Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities } from './file.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {doc} from "prettier";
import label = doc.builders.label;
import {data} from "autoprefixer";
import Select from 'react-select';
import {convertDateTimeFromServer, displayDefaultDateTime} from "app/shared/util/date-utils";
import {populateDependencyGraph} from "ts-loader/dist/utils";

export const File = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const fileList = useAppSelector(state => state.file.entities);
  const loading = useAppSelector(state => state.file.loading);


  const options = [

  ];
  const  deger =[

   ];

  const [selectedValue, setSelectedValue] = useState([]);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);


  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;


  const handleChange = (e) => {
    setSelectedValue(e);
  }
/* bu metod secili  degerleri deger dizisine aktarır bossa hepsini aktarır*/
  const secilidegerleripushla =()=>{
    if (selectedValue.length !==0){
      selectedValue.map((sc) => (
        deger.push(sc.id)       ))
    }

  }
/* databaseden gelen aday bilgilerini burada optios adlı diziye aktarıyoruz */

  const degerlerilistele =()=>{

    fileList && fileList.length > 0 ? (

        fileList.map((file) => (

         options.push({id: file.candidate.id ,label:file.candidate.firstName+' '+file.candidate.lastName,value:file.candidate.firstName+file.candidate.lastName })
       ))
      ) : (
        !loading && (
          <div >
          </div>
        )
     )
  }
  /* secilen  aday bilgilerini  aktarıyoruz */
  const secilidegerleriFiltrele=(ide)=>{

      if (deger.length !==0){

            for (let i = 0; i < deger.length; i++) {
              if (deger[i] ===ide) {
               return ide ;
              }
            }

      }  else{
         return (ide);
          }
     }



  return (

    <div>
      {degerlerilistele()}

      <h2 id="file-heading" data-cy="FileHeading">
        <Translate contentKey="iKaMetApp.file.home.title">Files</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="iKaMetApp.file.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="iKaMetApp.file.home.createLabel">Create new File</Translate>
          </Link>
        </div>
        <div >

          <h1>Aday Seç</h1>

            <div style={{width :'1000px'}}>
              <Select
                placeholder="Select Candidate"
                closeMenuOnSelect={false}
                isMulti
                options={options}
                className={"Select"}
                classNamePrefix={"Select"}
                onChange={handleChange}
                maxMenuHeight={300}
              />
            </div>
              {secilidegerleripushla()}

        </div>
        {''}
      </h2>

      <div className="table-responsive">
        {fileList && fileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="iKaMetApp.file.id">ID</Translate>
                </th>


                <th>
                  <Translate contentKey="iKaMetApp.file.candidate">Candidate</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.file.data">Data</Translate>
                </th>
                <th />
              </tr>
            </thead>

            <tbody>

              {fileList .filter(ide=> ide.candidate.id === secilidegerleriFiltrele(ide.candidate.id))
                .map((file, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${file.id}`} color="link" size="sm">
                      {file.id}
                    </Button>
                  </td>
                  <td>{file.candidate ? <Link to={`candidate/${file.candidate.id}`}>{file.candidate.firstName}{file.candidate.lastName} </Link> : ''}</td>


                  <td>
                    {file.data ? (
                      <div>
                        {file.dataContentType ? (
                          <a onClick={openFile(file.dataContentType, file.data)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {file.candidate.firstName}{"_"}{file.candidate.lastName}{"_"}{"cv"}
                        </span>
                      </div>
                    ) : null}
                  </td>

                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${file.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${file.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${file.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="iKaMetApp.file.home.notFound">No Files found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default File;
